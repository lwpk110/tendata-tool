/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.tendata.batch.webpower.item.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.util.StringUtils;


public class CsvDelimitedLineAggregator<T> extends ExtractorLineAggregator<T> {

	private String delimiter = ",";
	public static final char DEFAULT_QUOTE_CHARACTER = '\"';
	private char quoteCharacter = DEFAULT_QUOTE_CHARACTER;


	/**
	 * Public setter for the delimiter.
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String doAggregate(Object[] fields) {
		if(ArrayUtils.isNotEmpty(fields)){
			List<Object> objectList = Arrays.stream(fields).map(this::convert).collect(Collectors.toList());
			return StringUtils.arrayToDelimitedString(objectList.toArray(), this.delimiter);
		}
		return StringUtils.arrayToDelimitedString(fields, this.delimiter);
	}

	private Object convert(Object object){
		if(object instanceof String){
			String str = (String)object;
			str = quoteCharacter + str + quoteCharacter;
			return str;
		}
		return object;
	}

	public void setQuoteCharacter(char quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}
}
