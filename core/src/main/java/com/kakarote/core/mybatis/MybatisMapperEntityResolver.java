//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kakarote.core.mybatis;

import org.apache.ibatis.io.Resources;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MybatisMapperEntityResolver implements EntityResolver {
	private static final String IBATIS_CONFIG_SYSTEM = "ibatis-3-config.dtd";
	private static final String IBATIS_MAPPER_SYSTEM = "ibatis-3-mapper.dtd";
	private static final String MYBATIS_CONFIG_SYSTEM = "mybatis-3-config.dtd";
	private static final String MYBATIS_MAPPER_SYSTEM = "mybatis-3-mapper.dtd";

	private static final String MYBATIS_CONFIG_DTD = "org/apache/ibatis/builder/xml/mybatis-3-config.dtd";
	private static final String MYBATIS_MAPPER_DTD = "org/apache/ibatis/builder/xml/mybatis-3-mapper.dtd";

	public MybatisMapperEntityResolver() {
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
		try {
			if (systemId != null) {
				String lowerCaseSystemId = systemId.toLowerCase(Locale.ENGLISH);
				if (lowerCaseSystemId.contains(MYBATIS_CONFIG_SYSTEM) || lowerCaseSystemId.contains(IBATIS_CONFIG_SYSTEM)) {
					return getInputSource(MYBATIS_CONFIG_DTD, publicId, systemId);
				} else if (lowerCaseSystemId.contains(MYBATIS_MAPPER_SYSTEM) || lowerCaseSystemId.contains(IBATIS_MAPPER_SYSTEM)) {
					return getInputSource(MYBATIS_MAPPER_DTD, publicId, systemId);
				}else if (lowerCaseSystemId.contains("mybatis-3.5.3-mapper.dtd") || lowerCaseSystemId.contains("ibatis-3.5.3-mapper.dtd")) {
					return this.getInputSource("com/kakarote/core/mybatis/mybatis-3.5.3-mapper.dtd", publicId, systemId);
				}
			}

			return null;
		} catch (Exception var4) {
			throw new SAXException(var4.toString());
		}
	}

	private InputSource getInputSource(String path, String publicId, String systemId) {
		InputSource source = null;
		if (path != null) {
			try {
				InputStream in = Resources.getResourceAsStream(path);
				source = new InputSource(in);
				source.setPublicId(publicId);
				source.setSystemId(systemId);
			} catch (IOException var6) {
			}
		}

		return source;
	}
}
