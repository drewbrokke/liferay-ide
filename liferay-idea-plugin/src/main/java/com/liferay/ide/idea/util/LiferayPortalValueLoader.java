/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ide.idea.util;

import java.io.File;
import java.io.FilenameFilter;

import java.lang.reflect.Field;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Jiang
 * @author Gregory Amerson
 */
public class LiferayPortalValueLoader {

	public LiferayPortalValueLoader(Path appServerPortalDir, Path[] extraLibs) {
		_portalDir = appServerPortalDir;
		_userLibs = extraLibs;
	}

	public String[] loadHookPropertiesFromClass() {
		String loadClassName = "com.liferay.portal.deploy.hot.HookHotDeployListener"; //$NON-NLS-1$
		String fieldName = "SUPPORTED_PROPERTIES"; //$NON-NLS-1$

		return (String[]) _getFieldValuesFromClass(loadClassName, fieldName);
	}

	private void _addLibs(File libDir, List<URL> libUrlList) throws MalformedURLException {
		if (libDir.exists()) {
			File[] libs = libDir.listFiles(

				new FilenameFilter()
				{
					@Override
					public boolean accept(File dir, String fileName)
					{
						return fileName.toLowerCase().endsWith(".jar");
					}
				});

			if (! CoreUtil.isNullOrEmpty(libs)) {
				for (File portaLib : libs)
				{
					libUrlList.add(portaLib.toURI().toURL());
				}
			}
		}
	}

	private Object[] _getFieldValuesFromClass(String loadClassName, String fieldName) {
		Object[] retval = new Object[0];

		try {
			Class<?> classRef = _loadClass(loadClassName);
			Field propertiesField = classRef.getDeclaredField(fieldName);

			retval = (Object[]) (propertiesField.get(propertiesField));
		}
		catch (Exception e) {
		}

		return retval;
	}

	private Class<?> _loadClass(String className) throws Exception {
		List<URL> libUrlList = new ArrayList<>();

		if (_portalDir != null) {
			File libDir = Paths.get(_portalDir.toString(), "WEB-INF", "lib").toFile();

			_addLibs(libDir, libUrlList);
		}

		if (! CoreUtil.isNullOrEmpty(_userLibs)) {
			for (Path url : _userLibs) {
				libUrlList.add(url.toFile().toURI().toURL());
			}
		}

		URL[] urls = libUrlList.toArray(new URL[libUrlList.size()]);

		return new URLClassLoader(urls).loadClass(className);
	}

	private Path _portalDir;
	private Path[] _userLibs;

}