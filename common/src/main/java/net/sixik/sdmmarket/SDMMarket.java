package net.sixik.sdmmarket;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.sixik.sdmmarket.common.MarketEvents;
import net.sixik.sdmmarket.common.commands.MarketCommands;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.register.ItemsRegister;
import org.slf4j.Logger;

public class SDMMarket
{
	public static final String MOD_ID = "sdm_market";
	public static Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		MarketNetwork.init();
		ItemsRegister.ITEMS.register();
		MarketEvents.init();

		CommandRegistrationEvent.EVENT.register(MarketCommands::registerCommands);

		EnvExecutor.runInEnv(Env.CLIENT, () -> SDMMarketClient::init);
	}

	public static String moneyString(long money) {
		return String.format("◎ %,d", money);
	}

	public static String moneyString(String money) {
		return "◎ " + money;
	}

	public static void printStackTrace(String str, Throwable s){
		StringBuilder strBuilder = new StringBuilder(str);
		for (StackTraceElement stackTraceElement : s.getStackTrace()) {
			strBuilder.append("\t").append(" ").append("at").append(" ").append(stackTraceElement).append("\n");
		}
		str = strBuilder.toString();

		for (Throwable throwable : s.getSuppressed()) {
			printStackTrace(str, throwable);
		}

		Throwable ourCause = s.getCause();
		if(ourCause != null){
			printStackTrace(str, ourCause);
		}


		LOGGER.error(str);

	}
}
