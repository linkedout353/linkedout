package com.example.backend_v2.repositories;

import com.example.backend_v2.dao.JobPostDao;
import com.example.backend_v2.models.JobPost;
import com.example.backend_v2.models.JobPostAndCompany;
import com.example.backend_v2.repositories.rowMappers.JobPostAndCompanyRowMapper;
import com.example.backend_v2.repositories.rowMappers.JobPostRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;


import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class JobPostRepository implements JobPostDao {

    private final JdbcTemplate jdbcTemplate;

    public JobPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<JobPostAndCompany> getJobs() {
        /*var sql = """
                SELECT post_ID, date, content, job_title, workplace, jp.location AS location, position, jp.company_ID, name AS company_name
                FROM JobPost AS jp JOIN Company AS c ON jp.company_ID = c.company_ID
                WHERE date > CURRENT_DATE;
                """;*/
        var sql = """
                SELECT *
                FROM JobPost AS jp JOIN Company AS c ON jp.company_ID = c.company_ID
                WHERE date > CURRENT_DATE;
                """;
        return jdbcTemplate.query(sql, new JobPostAndCompanyRowMapper());
    }

    @Override
    public int insertJobPost(JobPost jobPost) {
        var sql = """
                INSERT INTO JobPost(post_id, date, content, job_title, company_id, workplace, position, profession)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?);
                """;
        return jdbcTemplate.update(sql, jobPost.post_ID(), jobPost.date(), jobPost.content(), jobPost.job_title(), jobPost.company_ID(), jobPost.workplace(), jobPost.position(), jobPost.profession());
    }

    @Override
    public int deleteJobPost(int id) {
        var sql = """
                DELETE FROM applies
                WHERE post_ID = ?;
                DELETE FROM JobPost
                WHERE post_ID = ?;
                """;
        return jdbcTemplate.update(sql, id, id);
    }

    @Override
    public Optional<JobPostAndCompany> getJobPostDetails(int id) {
        var sql = """
                SELECT *
                FROM JobPost AS jp JOIN Company AS c ON jp.company_ID = c.company_ID
                WHERE post_ID = ?
                """;
        return jdbcTemplate.query(sql, new JobPostAndCompanyRowMapper(), id).stream().findFirst();
    }

    @Override
    public List<JobPostAndCompany> filterJobs(String content, String job_title, String position, String workplace, String location) {
        var sql = """
                SELECT *
                FROM JobPost AS jp JOIN Company AS c ON jp.company_ID = c.company_ID
                WHERE (content LIKE CONCAT('%', ?, '%') OR job_title LIKE CONCAT('%', ?, '%') OR name LIKE CONCAT('%', ?, '%')) AND position LIKE CONCAT('%', ?, '%') AND workplace LIKE CONCAT('%', ?, '%') AND location LIKE CONCAT('%', ?, '%') AND date > CURRENT_DATE;
                """;
        //return jdbcTemplate.query(sql, new JobPostAndCompanyRowMapper(), content, job_title, content, position, workplace, location);
        return jdbcTemplate.query(sql, new JobPostAndCompanyRowMapper(), new SqlParameterValue(Types.VARCHAR, content),
                new SqlParameterValue(Types.VARCHAR, job_title),
                new SqlParameterValue(Types.VARCHAR, content),
                new SqlParameterValue(Types.VARCHAR, position),
                new SqlParameterValue(Types.VARCHAR, workplace),
                new SqlParameterValue(Types.VARCHAR, location));
    }

    @Override
    public List<JobPostAndCompany> getAppliedJobs(int id) {
        var sql = """
                SELECT *
                FROM (JobPost NATURAL JOIN Applies) AS jp JOIN Company AS c ON jp.company_ID = c.company_ID
                WHERE user_ID = ?
                """;
        return jdbcTemplate.query(sql, new JobPostAndCompanyRowMapper(), id);
    }

    @Override
    public int apply(int user_id, int post_id) {
        var sql = """
                INSERT INTO Applies (user_ID, post_ID)
                VALUES (?, ?);
                """;
        return jdbcTemplate.update(sql, user_id, post_id);
    }
}
