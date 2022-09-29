package com.example;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.repository.GenericRepository;

import io.micronaut.data.repository.PageableRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface SampleRepository extends PageableRepository<Sample, Integer> {

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true,
      countQuery = "select count(id) from sample where token = :token")
  Page<SampleProjection> getSamplesPage(String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true,
      countQuery = "select count(id) from sample where token = :token")
  Slice<SampleProjection> getSamplesSlice(String user, String token, Pageable page);

  @Query(
      value =
          "select s_.id as id, s_.name as name, s_.owner as owner, s_.token as token, s_.owner = :user as mine from sample s_ where s_.token = :token",
      nativeQuery = true,
      countQuery = "select count(id) from sample where token = :token")
  List<SampleProjection> getSamplesList(String user, String token, Pageable page);
}
