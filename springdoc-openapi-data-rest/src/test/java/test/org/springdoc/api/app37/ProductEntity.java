package test.org.springdoc.api.app37;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author bnasslahsen
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PRODUCT_ENTITY")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ProductEntity extends BaseEntity implements Comparable<ProductEntity> {

	/** SVUDI */
	private static final long serialVersionUID = 1L;

	/** 名字. */
	@NotNull
	@Column(nullable = false)
	private String name;

	/** 单价. */
	@NotNull
	@Column(nullable = false)
	private BigDecimal price;

	/** 日期. */
	@NotNull
	@Column(nullable = false)
	private LocalDate date;

	@Override
	public boolean isValid() {
		return name != null && price != null && date != null;
	}

	/** 根据日期排序 */
	@Override
	public int compareTo(ProductEntity oProduct) {
		return this.getDate().compareTo(oProduct.getDate());
	}

}
