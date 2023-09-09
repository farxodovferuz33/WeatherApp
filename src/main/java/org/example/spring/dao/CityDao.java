package org.example.spring.dao;

import lombok.NonNull;
import org.example.spring.domain.AuthUser;
import org.example.spring.domain.City;
import org.example.spring.domain.SubscribedCity;
import org.example.spring.domain.Weather;
import org.example.spring.dto.CityDTO;
import org.example.spring.dto.WeatherDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CityDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CityDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Integer save(@NonNull City city, Authentication authentication) {
        var sql = "insert into spring_jdbc.city(name, country, created_by) values(:name, :country, :created_by)";

        var paramSource = new MapSqlParameterSource()
                .addValue("name", city.getName())
                .addValue("country", city.getCountry())
                .addValue("created_by", authentication.getName());

        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});
        return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public List<City> getAllCity() {
        var sql = "select * from spring_jdbc.city order by id";
        var mapper = BeanPropertyRowMapper.newInstance(City.class);
        return namedParameterJdbcTemplate.query(sql, mapper);
    }

    public boolean deleteCity(int id) {
        var sql = "delete from spring_jdbc.City where id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, paramSource);
        return true;
    }

    public Optional<City> findById(int id) {
        var sql = "select * from spring_jdbc.city where id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        var rowMapper = BeanPropertyRowMapper.newInstance(City.class);
        City city = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
        return Optional.ofNullable(city);
    }

    public boolean update(Integer id, CityDTO cityDTO) {
        var sql = "update spring_jdbc.city set name = :name, country = :country where id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("name", cityDTO.name())
                .addValue("country", cityDTO.country())
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, paramSource);
        return true;
    }

    public List<Weather> getWeather(int id) {
        var sql = "select * from spring_jdbc.weather where city_id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        var mapper = BeanPropertyRowMapper.newInstance(Weather.class);
        return namedParameterJdbcTemplate.query(sql, paramSource, mapper);
    }

    public Integer deleteWeather(int id) {
        var sql = "delete from spring_jdbc.weather where id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("id", id);

        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"city_id"});
        return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("city_id");
    }

    public Integer addWeather(int cityId, WeatherDTO weatherDTO) {
        var sql = "insert into spring_jdbc.weather (city_id, celsius, fahrenheit, date)" +
                " values (:city_id, :celsius, :fahrenheit, :date);";
        var paramSource = new MapSqlParameterSource()
                .addValue("city_id", cityId)
                .addValue("celsius", weatherDTO.celsius())
                .addValue("fahrenheit", weatherDTO.fahrenheit())
                .addValue("date", weatherDTO.date());

        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"city_id"});
        return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("city_id");
    }

    public Integer subscribeCity(Long id, String cityName) {
        var sql = "insert into spring_jdbc.subscribedcities(user_id, city_name) values(:user_id, :city_name)";
        var paramSource = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("city_name", cityName);
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});
        return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public boolean checkSubscribedCity(int user_id, String cityName) {
        var sql = "select * from spring_jdbc.subscribedcities where user_id = :user_id and city_name = :city_name";
        var paramSource = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("city_name", cityName);

        var mapper = new BeanPropertyRowMapper<SubscribedCity>();
        SubscribedCity subscribedCity = namedParameterJdbcTemplate.queryForObject(sql, paramSource, mapper);
        return subscribedCity != null;
    }

    public List<SubscribedCity> getSubscribedCities(Long id) {
        var sql = "select * from spring_jdbc.subscribedcities where user_id = :id";
        var paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        var mapper = BeanPropertyRowMapper.newInstance(SubscribedCity.class);
        return namedParameterJdbcTemplate.query(sql, paramSource, mapper);
    }


//    public Optional<AuthUser> findByUsername(@NonNull String username) {
//        var sql = "select * from spring_jdbc.authuser t where t.username = :username and active = true";
//        var paramSource = new MapSqlParameterSource().addValue("username", username);
//        var rowMapper = BeanPropertyRowMapper.newInstance(AuthUser.class);
//        try {
//            var authUser = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
//            return Optional.of(authUser);
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//    }
}
