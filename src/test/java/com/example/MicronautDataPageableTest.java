package com.example;

import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Slice;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest
class MicronautDataPageableTest implements WithAssertions {

  @Inject EmbeddedApplication<?> application;

  @Inject
  @Client("/")
  HttpClient client;

  @Inject SampleRepository repo;

  @Test
  void testItWorks() {
    Assertions.assertTrue(application.isRunning());
  }

  @BeforeEach
  void setUp() {
    repo.saveAll(
        List.of(
            new Sample(0, "Sample 1", "me", "ignored"),
            new Sample(0, "Sample 2", "me", "found"),
            new Sample(0, "Sample 3", "you", "found"),
            new Sample(0, "Sample 4", "you", "ignored")));
  }

  @AfterEach
  void cleanUp() {
    repo.deleteAll();
  }

  @Test
  void page() {
    var uri =
        UriBuilder.of("/page").queryParam("owner", "me").queryParam("token", "found").toString();
    Page<Sample> page =
        client.toBlocking().retrieve(HttpRequest.GET(uri), Argument.of(Page.class, Sample.class));
    assertThat(page)
        .hasSize(1)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.owner()).isEqualTo("me")));
  }

  @Test
  void page2() {
    var uri =
        UriBuilder.of("/page2").queryParam("token", "found").toString();
    Page<Sample> page =
        client.toBlocking().retrieve(HttpRequest.GET(uri), Argument.of(Page.class, Sample.class));
    assertThat(page)
        .hasSize(2)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.owner()).isEqualTo("me")),
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.owner()).isEqualTo("you")));
  }

  @Test
  void slice() {
    var uri =
        UriBuilder.of("/slice").queryParam("owner", "me").queryParam("token", "found").toString();
    Slice<Sample> slice =
        client.toBlocking().retrieve(HttpRequest.GET(uri), Argument.of(Slice.class, Sample.class));
    assertThat(slice)
        .hasSize(1)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.owner()).isEqualTo("me")));
  }

  @Test
  void list() {
    var uri =
        UriBuilder.of("/list").queryParam("owner", "me").queryParam("token", "found").toString();
    List<Sample> list =
        client
            .toBlocking()
            .retrieve(HttpRequest.GET(uri), Argument.of(List.class, Argument.of(Sample.class)));
    assertThat(list)
        .hasSize(1)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.owner()).isEqualTo("me")));
  }

  @Test
  void pageProjections() {
    var uri =
        UriBuilder.of("/p/page").queryParam("owner", "me").queryParam("token", "found").toString();
    Page<SampleProjection> page =
        client
            .toBlocking()
            .retrieve(HttpRequest.GET(uri), Argument.of(Page.class, SampleProjection.class));
    assertThat(page)
        .hasSize(2)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isTrue()),
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isFalse()));
  }

  @Test
  void sliceProjections() {
    var uri =
        UriBuilder.of("/p/slice").queryParam("owner", "me").queryParam("token", "found").toString();
    Slice<SampleProjection> slice =
        client
            .toBlocking()
            .retrieve(HttpRequest.GET(uri), Argument.of(Slice.class, SampleProjection.class));
    assertThat(slice)
        .hasSize(2)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isTrue()),
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isFalse()));
  }

  @Test
  void listProjections() {
    var uri =
        UriBuilder.of("/p/list").queryParam("owner", "me").queryParam("token", "found").toString();
    List<SampleProjection> list =
        client
            .toBlocking()
            .retrieve(
                HttpRequest.GET(uri), Argument.of(List.class, Argument.of(SampleProjection.class)));
    assertThat(list)
        .hasSize(2)
        .satisfiesExactly(
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isTrue()),
            it ->
                assertThat(it)
                    .satisfies(
                        sample -> assertThat(sample.token()).isEqualTo("found"),
                        sample -> assertThat(sample.mine()).isFalse()));
  }
}
