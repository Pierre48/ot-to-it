package com.cgi.connect.mock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.model.PlcField;
import org.apache.plc4x.java.api.types.PlcResponseCode;
import org.apache.plc4x.java.api.value.PlcValue;

public class PlcReadResponseMock implements PlcReadResponse {
  @Override
  public PlcReadRequest getRequest() {
    return null;
  }

  @Override
  public Collection<String> getFieldNames() {
    return null;
  }

  @Override
  public PlcField getField(String s) {
    return null;
  }

  @Override
  public PlcResponseCode getResponseCode(String s) {
    return null;
  }

  @Override
  public PlcValue getAsPlcValue() {
    return null;
  }

  @Override
  public PlcValue getPlcValue(String s) {
    return null;
  }

  @Override
  public int getNumberOfValues(String s) {
    return 0;
  }

  @Override
  public Object getObject(String s) {
    return null;
  }

  @Override
  public Object getObject(String s, int i) {
    return null;
  }

  @Override
  public Collection<Object> getAllObjects(String s) {
    return null;
  }

  @Override
  public boolean isValidBoolean(String s) {
    return false;
  }

  @Override
  public boolean isValidBoolean(String s, int i) {
    return false;
  }

  @Override
  public Boolean getBoolean(String s) {
    return null;
  }

  @Override
  public Boolean getBoolean(String s, int i) {
    return null;
  }

  @Override
  public Collection<Boolean> getAllBooleans(String s) {
    return null;
  }

  @Override
  public boolean isValidByte(String s) {
    return false;
  }

  @Override
  public boolean isValidByte(String s, int i) {
    return false;
  }

  @Override
  public Byte getByte(String s) {
    return null;
  }

  @Override
  public Byte getByte(String s, int i) {
    return null;
  }

  @Override
  public Collection<Byte> getAllBytes(String s) {
    return null;
  }

  @Override
  public boolean isValidShort(String s) {
    return false;
  }

  @Override
  public boolean isValidShort(String s, int i) {
    return false;
  }

  @Override
  public Short getShort(String s) {
    return null;
  }

  @Override
  public Short getShort(String s, int i) {
    return null;
  }

  @Override
  public Collection<Short> getAllShorts(String s) {
    return null;
  }

  @Override
  public boolean isValidInteger(String s) {
    return false;
  }

  @Override
  public boolean isValidInteger(String s, int i) {
    return false;
  }

  @Override
  public Integer getInteger(String s) {
    return null;
  }

  @Override
  public Integer getInteger(String s, int i) {
    return null;
  }

  @Override
  public Collection<Integer> getAllIntegers(String s) {
    return null;
  }

  @Override
  public boolean isValidBigInteger(String s) {
    return false;
  }

  @Override
  public boolean isValidBigInteger(String s, int i) {
    return false;
  }

  @Override
  public BigInteger getBigInteger(String s) {
    return null;
  }

  @Override
  public BigInteger getBigInteger(String s, int i) {
    return null;
  }

  @Override
  public Collection<BigInteger> getAllBigIntegers(String s) {
    return null;
  }

  @Override
  public boolean isValidLong(String s) {
    return false;
  }

  @Override
  public boolean isValidLong(String s, int i) {
    return false;
  }

  @Override
  public Long getLong(String s) {
    return null;
  }

  @Override
  public Long getLong(String s, int i) {
    return null;
  }

  @Override
  public Collection<Long> getAllLongs(String s) {
    return null;
  }

  @Override
  public boolean isValidFloat(String s) {
    return false;
  }

  @Override
  public boolean isValidFloat(String s, int i) {
    return false;
  }

  @Override
  public Float getFloat(String s) {
    return null;
  }

  @Override
  public Float getFloat(String s, int i) {
    return null;
  }

  @Override
  public Collection<Float> getAllFloats(String s) {
    return null;
  }

  @Override
  public boolean isValidDouble(String s) {
    return false;
  }

  @Override
  public boolean isValidDouble(String s, int i) {
    return false;
  }

  @Override
  public Double getDouble(String s) {
    return null;
  }

  @Override
  public Double getDouble(String s, int i) {
    return null;
  }

  @Override
  public Collection<Double> getAllDoubles(String s) {
    return null;
  }

  @Override
  public boolean isValidBigDecimal(String s) {
    return false;
  }

  @Override
  public boolean isValidBigDecimal(String s, int i) {
    return false;
  }

  @Override
  public BigDecimal getBigDecimal(String s) {
    return null;
  }

  @Override
  public BigDecimal getBigDecimal(String s, int i) {
    return null;
  }

  @Override
  public Collection<BigDecimal> getAllBigDecimals(String s) {
    return null;
  }

  @Override
  public boolean isValidString(String s) {
    return false;
  }

  @Override
  public boolean isValidString(String s, int i) {
    return false;
  }

  @Override
  public String getString(String s) {
    return null;
  }

  @Override
  public String getString(String s, int i) {
    return null;
  }

  @Override
  public Collection<String> getAllStrings(String s) {
    return null;
  }

  @Override
  public boolean isValidTime(String s) {
    return false;
  }

  @Override
  public boolean isValidTime(String s, int i) {
    return false;
  }

  @Override
  public LocalTime getTime(String s) {
    return null;
  }

  @Override
  public LocalTime getTime(String s, int i) {
    return null;
  }

  @Override
  public Collection<LocalTime> getAllTimes(String s) {
    return null;
  }

  @Override
  public boolean isValidDate(String s) {
    return false;
  }

  @Override
  public boolean isValidDate(String s, int i) {
    return false;
  }

  @Override
  public LocalDate getDate(String s) {
    return null;
  }

  @Override
  public LocalDate getDate(String s, int i) {
    return null;
  }

  @Override
  public Collection<LocalDate> getAllDates(String s) {
    return null;
  }

  @Override
  public boolean isValidDateTime(String s) {
    return false;
  }

  @Override
  public boolean isValidDateTime(String s, int i) {
    return false;
  }

  @Override
  public LocalDateTime getDateTime(String s) {
    return null;
  }

  @Override
  public LocalDateTime getDateTime(String s, int i) {
    return null;
  }

  @Override
  public Collection<LocalDateTime> getAllDateTimes(String s) {
    return null;
  }
}
