package me.huangduo.hms.dao.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.huangduo.hms.exceptions.JsonDeserializationException;
import me.huangduo.hms.exceptions.JsonSerializationException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericJsonTypeHandler<T extends Serializable> extends BaseTypeHandler<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public GenericJsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException(e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException(e);
        }
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException(e);
        }
    }
}
