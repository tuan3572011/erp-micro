package com.erp.rest.base;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("api")
@SwaggerDefinition(info = @Info(description = "This API will be used in eFD projects.", version = "V1.0.0", title = "eFD REST API", termsOfService = "private"), schemes = {
        SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS })
public class BaseRestfulApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Collections.emptySet();
    }
}
