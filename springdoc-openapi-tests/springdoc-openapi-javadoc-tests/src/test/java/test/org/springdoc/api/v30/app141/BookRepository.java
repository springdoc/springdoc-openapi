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

package test.org.springdoc.api.v30.app141;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * The type Book repository.
 */
@Component
class BookRepository {

	/**
	 * Find by author list.
	 *
	 * @param author the author
	 * @return the list
	 */
	List<Book> findByAuthor(String author) {
		Book[] books = { new Book("1", "title1", "author1") };
		return Arrays.asList(books);
	}

	/**
	 * Find all list.
	 *
	 * @return the list
	 */
	List<Book> findAll() {
		Book[] books = { new Book("2", "title2", "author2") };
		return Arrays.asList(books);
	}
}
