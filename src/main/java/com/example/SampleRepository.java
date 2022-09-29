package com.example;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface SampleRepository extends PageableRepository<Sample, Integer> {

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token from sample s_ where s_.token = :token and s_.owner = :user",
      nativeQuery = true,
      countQuery = "select count(id) from sample where token = :token and owner = :user")
  Page<Sample> getSamplesPage(@Nullable String user, String token, Pageable page);

  @Query(
      value = "select * from sample where token = :token1 or token = :token2",
      nativeQuery = true,
      countQuery = "select count(*) from sample where token = :token1")
  Page<Sample> getSamplesPageContrived(String token1, String token2, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token from sample s_ where s_.token = :token and s_.owner = :user",
      nativeQuery = true)
  Slice<Sample> getSamplesSlice(@Nullable String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token from sample s_ where s_.token = :token and s_.owner = :user",
      nativeQuery = true)
  List<Sample> getSamplesList(@Nullable String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true,
      // Switching to this (with the no-op use of extra param) compiles but doesn't return correct
      // results.
      // countQuery = "select count(id) from sample where token = :token and if(:user is null, 1,
      // 1)"
      countQuery = "select count(id) from sample where token = :token")
  Page<SampleProjection> getSamplesPageProjection(
      @Nullable String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true)
  Slice<SampleProjection> getSamplesSliceProjection(
      @Nullable String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true)
  List<SampleProjection> getSamplesListProjection(
      @Nullable String user, String token, Pageable page);
}
