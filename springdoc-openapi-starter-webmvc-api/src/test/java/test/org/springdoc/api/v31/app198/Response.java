package test.org.springdoc.api.v31.app198;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author bnasslahsen
 */
public class Response {

	public Response(BigDecimal val1, BigDecimal val2, BigDecimal val3) {
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
	}

	@Schema(deprecated = true)
	@Deprecated
	private BigDecimal val1;

	private BigDecimal val2;

	private BigDecimal val3;

	public BigDecimal getVal1() {
		return val1;
	}

	public void setVal1(BigDecimal val1) {
		this.val1 = val1;
	}

	public BigDecimal getVal2() {
		return val2;
	}

	public void setVal2(BigDecimal val2) {
		this.val2 = val2;
	}

	public BigDecimal getVal3() {
		return val3;
	}

	public void setVal3(BigDecimal val3) {
		this.val3 = val3;
	}
}
