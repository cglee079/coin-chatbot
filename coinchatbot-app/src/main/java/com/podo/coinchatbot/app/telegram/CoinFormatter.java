package com.podo.coinchatbot.app.telegram;


import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CoinFormatter {
	private int digitKRW;
	private int digitUSD;
	private int digitBTC;

	public CoinFormatter(int digitKRW, int digitUSD, int digitBTC) {
		this.digitKRW = digitKRW;
		this.digitUSD = digitUSD;
		this.digitBTC = digitBTC;
	}

	public String toExchangeRateKRWStr(BigDecimal krw) {
		DecimalFormat df = new DecimalFormat("#,###");
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(0);
		df.setPositiveSuffix("원");
		df.setNegativeSuffix("원");
		return df.format(krw);
	}

	public String toBTCStr(BigDecimal btc) {
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMinimumFractionDigits(digitBTC);
		df.setMaximumFractionDigits(digitBTC);
		df.setPositiveSuffix(" BTC");
		df.setNegativeSuffix(" BTC");
		return df.format(btc);
	}

	public String toOnlyBTCMoneyStr(BigDecimal money, Market marketId) {
		String result = "";

		if (marketId.isKRW()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositiveSuffix("원");
			result = df.format(money);
		} else if (marketId.isUSD()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositivePrefix("$");
			result = df.format(money);
		}
		return result;
	}

	public String toInvestAmountStr(BigDecimal invest, Market marketId) {
		String result = "";

		if (marketId.isKRW()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositiveSuffix("원");
			result = df.format(invest);
		} else if (marketId.isUSD()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositivePrefix("$");
			result = df.format(invest);
		}
		return result;
	}

	public String toSignInvestAmountStr(BigDecimal invest, Market market) {
		String result = "";

		if (market.isKRW()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositivePrefix("+");
			df.setNegativePrefix("-");
			df.setPositiveSuffix("원");
			df.setNegativeSuffix("원");
			result = df.format(invest);
		} else if (market.isUSD()) {
			DecimalFormat df = new DecimalFormat("#,###");
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositivePrefix("+$");
			df.setNegativePrefix("-$");
			result = df.format(invest);
		}

		return result;
	}

	public String toMoneyStr(BigDecimal money, Market market) {
		String result = "";
		if (market.isKRW()) {
			result = toKRWStr(money);
		}
		if (market.isUSD()) {
			result = toUSDStr(money);
		}
		return result;
	}

	public String toSignMoneyStr(BigDecimal i, Market marketId) {
		String result = "";
		if (marketId.isKRW()) {
			result = toSignKRWStr(i);
		}
		if (marketId.isUSD()) {
			result = toSignUSDStr(i);
		}
		return result;
	}

	public String toKRWStr(BigDecimal money) {
		DecimalFormat df = new DecimalFormat("#,###.#");
		df.setMinimumFractionDigits(digitKRW);
		df.setMaximumFractionDigits(digitKRW);
		df.setPositiveSuffix("원");
		df.setNegativeSuffix("원");
		return df.format(money);
	}

	public String toSignKRWStr(BigDecimal money) {
		DecimalFormat df = new DecimalFormat("#,###.#");
		df.setMinimumFractionDigits(digitKRW);
		df.setMaximumFractionDigits(digitKRW);
		df.setPositivePrefix("+");
		df.setNegativePrefix("-");
		df.setPositiveSuffix("원");
		df.setNegativeSuffix("원");
		return df.format(money);
	}

	public String toUSDStr(BigDecimal usd) {
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMinimumFractionDigits(digitUSD);
		df.setMaximumFractionDigits(digitUSD);
		df.setPositivePrefix("$");
		return df.format(usd);
	}

	public String toSignUSDStr(BigDecimal usd) {
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMinimumFractionDigits(digitUSD);
		df.setMaximumFractionDigits(digitUSD);
		df.setPositivePrefix("+$");
		df.setNegativePrefix("-$");
		return df.format(usd);
	}

	public String toVolumeStr(BigDecimal i) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(i);
	}

	public String toSignVolumeStr(BigDecimal i) {
		DecimalFormat df = new DecimalFormat("#,###");
		df.setPositivePrefix("+");
		df.setNegativePrefix("-");
		return df.format(i);
	}

	public String toCoinCntStr(BigDecimal coinCount, Language language) {
		DecimalFormat df = new DecimalFormat("#.##");
		if (language.equals(Language.KR)) {
			df.setPositiveSuffix("개");
			df.setNegativeSuffix("개");
		} else if (language.equals(Language.EN)) {
			df.setPositiveSuffix(" COIN");
			df.setNegativeSuffix(" COIN");
		}

		return df.format(coinCount);
	}

	public String toSignKimpStr(double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setPositivePrefix("+");
		df.setNegativePrefix("-");
		return df.format(d);
	}

	public String toSignPercentStr(BigDecimal c, BigDecimal b) {
		String prefix = "";
		BigDecimal gap = c.subtract(b);
		BigDecimal percent = gap.divide(b, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
		if (percent.compareTo(BigDecimal.ZERO) >= 0) {
			prefix = "+";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		return prefix + df.format(percent) + "%";
	}


	public BigDecimal formatPrice(BigDecimal price, Market market) {
		DecimalFormat df = null;
		if (market.isKRW()) {
			df = new DecimalFormat("####.#");
			df.setMinimumFractionDigits(digitKRW);
			df.setMaximumFractionDigits(digitKRW);
		}

		if (market.isUSD()) {
			df = new DecimalFormat("#.#");
			df.setMinimumFractionDigits(digitUSD);
			df.setMaximumFractionDigits(digitUSD);
		}
		return BigDecimal.valueOf(Double.valueOf(df.format(price)));
	}
}
