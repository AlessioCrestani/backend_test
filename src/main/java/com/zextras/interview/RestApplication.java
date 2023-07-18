package com.zextras.interview;

import com.zextras.interview.App.RestAPI;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Allows to initialize the resources of each API.
 */
@ApplicationPath("/")
public class RestApplication extends Application {

  @Override
  public Set<Object> getSingletons() {
    return Set.of(new RestAPI());
  }
}
