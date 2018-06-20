/**
 * File Name: ConfigHelper.java
 * 
 * Copyright (c) 2018 BISON Schweiz AG, All Rights Reserved.
 */

package to.rtc.cli.migrate.command;

import com.ibm.team.filesystem.cli.core.subcommands.CommonOptions;
import com.ibm.team.filesystem.cli.core.subcommands.IScmClientConfiguration;
import com.ibm.team.filesystem.cli.core.util.ConnectionInfo;
import com.ibm.team.filesystem.client.FileSystemException;
import com.ibm.team.rtc.cli.infrastructure.internal.parser.ICommandLine;
import com.ibm.team.rtc.cli.infrastructure.internal.parser.IOptionKey;

/**
 * Helper class to access client configuration properties.
 *
 * @author Patrick Reinhart
 */
final class ConfigHelper {
	private ConfigHelper() {
	}

	static String getURI(IScmClientConfiguration config) {
		return getOption(config, CommonOptions.OPT_URI, new Fallback() {
			@Override
			public String apply(ConnectionInfo connectionInfo) {
				return connectionInfo.getURI();
			}
		});
	}

	static String getUsername(IScmClientConfiguration config) {
		return getOption(config, CommonOptions.OPT_USERNAME, new Fallback() {
			@Override
			public String apply(ConnectionInfo connectionInfo) {
				return connectionInfo.getUsername();
			}
		});
	}

	static String getPassword(IScmClientConfiguration config) {
		return getOption(config, CommonOptions.OPT_PASSWORD, new Fallback() {
			@Override
			public String apply(ConnectionInfo connectionInfo) {
				return connectionInfo.getPassword();
			}
		});
	}

	private static String getOption(IScmClientConfiguration config, IOptionKey key, Fallback fallback) {
		ICommandLine subcommandCommandLine = config.getSubcommandCommandLine();
		if (subcommandCommandLine.hasOption(key)) {
			return subcommandCommandLine.getOption(key);
		}
		try {
			return fallback.apply(config.getConnectionInfo());
		} catch (FileSystemException e) {
			throw new RuntimeException("Unable to get " + key.getName(), e);
		}
	}

	interface Fallback {
		String apply(ConnectionInfo connectionInfo);
	}
}
