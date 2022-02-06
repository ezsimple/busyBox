package io.mkeasy.utils;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
 
public class LowerKeyMap extends ListOrderedMap {

	private static final long serialVersionUID = -1084066205099270702L;

	/**
     * ListOrderedMap을 상속받는 클래스를 생성하고 put 함수를 가로채 key를 lowerCase로 치환해주는 새로운 Map 객체를 만들어서 사용하는 것이다.
     * 이유는 전자정부프레임워크 역시 후자를 택하고 있기 때문에 어느정도 검증된 방법이라 생각했다.
	 * 전자정부의 EgovMap 을 살펴보면 ListOrderedMap 를 상속받고 camelCase 로 key를 셋팅하도록 되어있다.
     */
    public Object put(Object key, Object value) {
        return super.put(StringUtils.lowerCase((String) key), value);
    }
}