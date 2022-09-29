package com.example;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import reactor.tools.agent.ReactorDebugAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  static {
    ReactorDebugAgent.init();
  }

  private final SampleRepository repo;

  @Inject
  public Application(SampleRepository repo) {this.repo = repo;}

  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }

  @Get("/page")
  Page<SampleProjection> page(@QueryValue String owner, @Nullable @QueryValue String token, Pageable page) {
    log.debug("/page?start={}&token={} [page={}]", owner, token, page);
    return repo.getSamplesPage(owner, token, page);
  }

  @Get("/slice")
  Slice<SampleProjection> slice(@QueryValue String owner, @Nullable @QueryValue String token, Pageable page) {
    log.debug("/slice?start={}&token={} [page={}]", owner, token, page);
    return repo.getSamplesSlice(owner, token, page);
  }

  @Get("/list")
  List<SampleProjection> list(@QueryValue String owner, @Nullable @QueryValue String token, Pageable page) {
    log.debug("/list?start={}&token={} [page={}]", owner, token, page);
    return repo.getSamplesList(owner, token, page);
  }
}
