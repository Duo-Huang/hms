package me.huangduo.hms.dao.handler;

import me.huangduo.hms.enums.SingleValueEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GenericEnumValueHandler<E extends Enum<E> & SingleValueEnum> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public GenericEnumValueHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return getEnumFromValue(value);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return getEnumFromValue(value);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return getEnumFromValue(value);
    }

    private E getEnumFromValue(int value) {
        for (E enumConstant : type.getEnumConstants()) {
            if (enumConstant.getValue() == value) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Invalid value for enum " + type.getSimpleName() + ": " + value);
    }
}
