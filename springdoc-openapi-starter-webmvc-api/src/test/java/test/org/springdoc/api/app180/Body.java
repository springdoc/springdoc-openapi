package test.org.springdoc.api.app180;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "Body", description = "Body", example = "{\"key\":\"value\"}")
public class Body implements Map<String, Object>, MapExclusion {

    @Schema(hidden = true)
    private Map<String, Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public Object get(Object key) {
		return null;
	}

	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public Object remove(Object key) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return null;
	}
}
