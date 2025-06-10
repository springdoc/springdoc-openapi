/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app37;

/**
 *
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品信息
 *
 * @author zctmdc
 */
@RepositoryRestResource(path = "product")
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	List<ProductEntity> findByPrice(@Parameter(
			name = "price",
			description = "test desc",
			in = ParameterIn.QUERY,
			required = true
	)
	@Param("price") BigDecimal price);

	/**
	 * 根据商品名称查询商品信息
	 *
	 * @param name 商品名称
	 * @return 商品信息列表
	 */
	List<ProductEntity> findByName(@Parameter(name = "name2", required = true) String name);

	/**
	 * 根据商品名称查询最新的商品信息
	 *
	 * @param name 商品名称
	 * @return 商品信息
	 */
	ProductEntity findTopByNameOrderByDateDesc(@Parameter(name = "name2", required = true) String name);

	/**
	 * 根据商品名称模糊查询商品信息并按时间降序排序
	 *
	 * @param name 商品名称
	 * @return 商品信息列表
	 */
	List<ProductEntity> findByNameContainingIgnoreCaseOrderByDateDesc(
			@Parameter(name = "name2", required = true) String name);

	/**
	 * 根据商品名称模糊查询商品信息并按时间降序排序
	 *
	 * @param name 商品名称
	 * @return 商品信息列表
	 */
	List<ProductEntity> findByNameContainingIgnoreCase(
			@Parameter(name = "name2", required = true) String name);

	/**
	 * 根据商品名称模糊查询指定日期前的商品信息
	 *
	 * @param end 指定日期结束
	 * @return 商品信息列表
	 */
	List<ProductEntity> findByDateBefore(
			@Parameter(name = "end2", required = true) @DateTimeFormat(pattern = DateTokenConverter.DEFAULT_DATE_PATTERN) @Param("end") LocalDate end);

	/**
	 * 根据商品名称模糊查询指定日期前的商品信息
	 *
	 * @param name 商品名称
	 * @param end  指定日期结束
	 * @return 商品信息列表
	 */
	List<ProductEntity> findByNameContainingIgnoreCaseAndDateBefore(
			@Parameter(name = "name2", required = true) String name,
			@Parameter(name = "end2", required = true) @DateTimeFormat(pattern = DateTokenConverter.DEFAULT_DATE_PATTERN) @Param("end") LocalDate end);

}