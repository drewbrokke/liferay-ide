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

package com.liferay.ide.idea.ui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

import com.liferay.ide.idea.ui.LiferayIdeaUI;

/**
 * @author Andy Wu
 */
public class InitBundleAction extends AbstractLiferayGradleTaskAction {

	public InitBundleAction() {
		super("InitBundle", "Run initBundle task", LiferayIdeaUI.LIFERAY_ICON);
	}

	@Override
	public boolean isEnableAndVisible(AnActionEvent e) {
		Project project = e.getProject();

		if (project.getBaseDir().equals(getVirtualFile(e))) {
			return true;
		}

		return false;
	}

	@Override
	protected String getTaskName() {
		return "initBundle";
	}

	@Override
	protected String getWorkDirectory(AnActionEvent e) {
		Project project = e.getRequiredData(CommonDataKeys.PROJECT);

		return project.getBasePath();
	}

}