package org.springdoc.core.converters;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.MonetaryContext;
import javax.money.NumberValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonCreator;

@Schema
public abstract class MonetaryAmount implements javax.money.MonetaryAmount {
	
	@JsonCreator
    public MonetaryAmount(@JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currencyAbbr)
    {
      this.amount = amount;
      this.currencyAbbr = currencyAbbr;
      this.delegate=delegateImpl(amount, currencyAbbr);
      
    }

	protected abstract javax.money.MonetaryAmount delegateImpl(BigDecimal amount, String currencyAbbr);
	
	@JsonProperty("amount")
	@Schema(example = "99.96")
	private BigDecimal amount;
	
	@JsonProperty("currency")
	@Schema(example = "USD")
	private String currencyAbbr;
	
	@JsonIgnore
	private javax.money.MonetaryAmount delegate;
	
	@JsonIgnore
	private boolean zero;
	@JsonIgnore
	private boolean negative;
	@JsonIgnore
	private boolean positive;
	@JsonIgnore
	private boolean negativeOrZero;
	@JsonIgnore
	private boolean positiveOrZero;

	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrencyAbbr() {
		return currencyAbbr;
	}

	public void setCurrencyAbbr(String currencyAbbr) {
		this.currencyAbbr = currencyAbbr;
	}

	
	
	@JsonIgnore
	public CurrencyUnit getCurrency() {
		return delegate.getCurrency();
	}
	@JsonIgnore
	public NumberValue getNumber() {
		return delegate.getNumber();
	}

	public int compareTo(javax.money.MonetaryAmount o) {
		return delegate.compareTo(o);
	}
	@JsonIgnore
	public MonetaryContext getContext() {
		return delegate.getContext();
	}
	@JsonIgnore
	public javax.money.MonetaryAmountFactory<? extends javax.money.MonetaryAmount> getFactory() {
		return delegate.getFactory();
	}

	public boolean isGreaterThan(javax.money.MonetaryAmount amount) {
		return delegate.isGreaterThan(amount);
	}

	public boolean isGreaterThanOrEqualTo(javax.money.MonetaryAmount amount) {
		return delegate.isGreaterThanOrEqualTo(amount);
	}

	public boolean isLessThan(javax.money.MonetaryAmount amount) {
		return delegate.isLessThan(amount);
	}

	public boolean isLessThanOrEqualTo(javax.money.MonetaryAmount amt) {
		return delegate.isLessThanOrEqualTo(amt);
	}

	public boolean isEqualTo(javax.money.MonetaryAmount amount) {
		return delegate.isEqualTo(amount);
	}
	@JsonIgnore
	public int signum() {
		return delegate.signum();
	}

	public javax.money.MonetaryAmount add(javax.money.MonetaryAmount amount) {
		return delegate.add(amount);
	}

	public javax.money.MonetaryAmount subtract(javax.money.MonetaryAmount amount) {
		return delegate.subtract(amount);
	}

	public javax.money.MonetaryAmount multiply(long multiplicand) {
		return delegate.multiply(multiplicand);
	}

	public javax.money.MonetaryAmount multiply(double multiplicand) {
		return delegate.multiply(multiplicand);
	}

	public javax.money.MonetaryAmount multiply(Number multiplicand) {
		return delegate.multiply(multiplicand);
	}

	public javax.money.MonetaryAmount divide(long divisor) {
		return delegate.divide(divisor);
	}

	public javax.money.MonetaryAmount divide(double divisor) {
		return delegate.divide(divisor);
	}

	public javax.money.MonetaryAmount divide(Number divisor) {
		return delegate.divide(divisor);
	}

	public javax.money.MonetaryAmount remainder(long divisor) {
		return delegate.remainder(divisor);
	}

	public javax.money.MonetaryAmount remainder(double divisor) {
		return delegate.remainder(divisor);
	}

	public javax.money.MonetaryAmount remainder(Number divisor) {
		return delegate.remainder(divisor);
	}

	public javax.money.MonetaryAmount[] divideAndRemainder(long divisor) {
		return delegate.divideAndRemainder(divisor);
	}

	public javax.money.MonetaryAmount[] divideAndRemainder(double divisor) {
		return delegate.divideAndRemainder(divisor);
	}

	public javax.money.MonetaryAmount[] divideAndRemainder(Number divisor) {
		return delegate.divideAndRemainder(divisor);
	}

	public javax.money.MonetaryAmount divideToIntegralValue(long divisor) {
		return delegate.divideToIntegralValue(divisor);
	}

	public javax.money.MonetaryAmount divideToIntegralValue(double divisor) {
		return delegate.divideToIntegralValue(divisor);
	}

	public javax.money.MonetaryAmount divideToIntegralValue(Number divisor) {
		return delegate.divideToIntegralValue(divisor);
	}

	public javax.money.MonetaryAmount scaleByPowerOfTen(int power) {
		return delegate.scaleByPowerOfTen(power);
	}
	@JsonIgnore
	public javax.money.MonetaryAmount abs() {
		return delegate.abs();
	}
	@JsonIgnore
	public javax.money.MonetaryAmount negate() {
		return delegate.negate();
	}
	@JsonIgnore
	public javax.money.MonetaryAmount plus() {
		return delegate.plus();
	}
	@JsonIgnore
	public javax.money.MonetaryAmount stripTrailingZeros() {
		return delegate.stripTrailingZeros();
	}
}
