package me.huangduo.hms.dao.handler;

import me.huangduo.hms.enums.HmsRoleType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleTypeHandler extends BaseTypeHandler<HmsRoleType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, HmsRoleType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public HmsRoleType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return HmsRoleType.fromValue(value);
    }

    @Override
    public HmsRoleType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return HmsRoleType.fromValue(value);
    }

    @Override
    public HmsRoleType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return HmsRoleType.fromValue(value);
    }
}

