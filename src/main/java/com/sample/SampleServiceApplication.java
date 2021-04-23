package com.sample;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.sample.configuration.beans.CoreBeansConfiguration;
import com.sample.dto.common.validator.mapper.ConstraintViolationExceptionMapper;
import com.sample.dto.common.validator.mapper.JerseyViolationExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.session.SessionHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

@Slf4j
public class SampleServiceApplication extends Application<SampleServiceConfiguration> {
    public static final String NAME = "sample-service";
    @Getter
    private AnnotationConfigApplicationContext context;

    public static void main(final String[] args) throws Exception {
        new SampleServiceApplication().run(args);
    }

    public Class<?> getBeansConfigurationClass() {
        return CoreBeansConfiguration.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize(final Bootstrap<SampleServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<SampleServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SampleServiceConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)));
    }

    private void refreshContext() {
        if (!context.isActive()) {
            log.info("Refreshing context");
            context.refresh();
            log.info("Refreshed context successfully");
        }
    }

    private void registerContext() {
        context = new AnnotationConfigApplicationContext();
        context.setDisplayName(getName());
        log.info("Registering core configuration class");
        context.register(getBeansConfigurationClass());
    }

    private void registerHealthChecks(HealthCheckRegistry healthCheckRegistry) {
        log.info("Registering healthchecks");

        context.getBeansOfType(HealthCheck.class).values().forEach(healthCheck -> {
            String name = healthCheck.getClass().getSimpleName();
            log.info("Registering healthcheck {} ", name);
            healthCheckRegistry.register(name, healthCheck);
        });
    }

    private void registerProviders(JerseyEnvironment jerseyEnvironment) {
        log.info("Registering providers with annotation {} with Jersey", Provider.class.getSimpleName());

        context.getBeansWithAnnotation(Provider.class).values().forEach(component -> {
            log.info("Registering provider {}", component);
            jerseyEnvironment.register(component);
        });
    }

    private void registerResources(JerseyEnvironment jerseyEnvironment) {
        log.info("Registering JAX-RS resources with annotation {} with Jersey", Path.class.getSimpleName());

        context.getBeansWithAnnotation(Path.class).values().forEach(component -> {
            log.info("Registering resource {}", component);
            jerseyEnvironment.register(component);
        });
    }

    private void registerSingletons(final Environment environment) {
        context.getBeanFactory().registerSingleton("HealthCheckRegistry", environment.healthChecks());
    }

    @Override
    public void run(final SampleServiceConfiguration configuration, final Environment environment) {
        registerContext();
        registerSingletons(environment);
        refreshContext();

        environment.servlets().setSessionHandler(new SessionHandler());

        JerseyEnvironment jerseyEnvironment = environment.jersey();

        registerResources(jerseyEnvironment);
        registerProviders(jerseyEnvironment);
        registerHealthChecks(environment.healthChecks());

        jerseyEnvironment.register(new ConstraintViolationExceptionMapper());
        jerseyEnvironment.register(new JerseyViolationExceptionMapper());
    }
}
