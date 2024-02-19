//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xxin.goods.util;

import com.alibaba.excel.util.StringUtils;

public class MoneyConvertUtil {
    private static final String[] NUMBERS = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] IUNIT = new String[]{"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
    private static final String[] DUNIT = new String[]{"角", "分", "厘"};

    public MoneyConvertUtil() {
    }

    public static String toChinese(String str) {
        if (!StringUtils.isBlank(str) && str.matches("(-)?[\\d]*(.)?[\\d]*")) {
            if (!"0".equals(str) && !"0.00".equals(str) && !"0.0".equals(str)) {
                boolean flag = false;
                if (str.startsWith("-")) {
                    flag = true;
                    str = str.replaceAll("-", "");
                }

                str = str.replaceAll(",", "");
                String integerStr;
                String decimalStr;
                if (str.indexOf(".") > 0) {
                    integerStr = str.substring(0, str.indexOf("."));
                    decimalStr = str.substring(str.indexOf(".") + 1);
                } else if (str.indexOf(".") == 0) {
                    integerStr = "";
                    decimalStr = str.substring(1);
                } else {
                    integerStr = str;
                    decimalStr = "";
                }

                if (integerStr.length() > IUNIT.length) {
                    System.out.println(str + "：超出计算能力");
                    return str;
                } else {
                    int[] integers = toIntArray(integerStr);
                    if (integers.length > 1 && integers[0] == 0) {
                        System.out.println("抱歉，请输入数字！");
                        if (flag) {
                            str = "-" + str;
                        }

                        return str;
                    } else {
                        boolean isWan = isWan5(integerStr);
                        int[] decimals = toIntArray(decimalStr);
                        String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);
                        return flag ? "负" + result : result;
                    }
                }
            } else {
                return "零元";
            }
        } else {
            System.out.println("抱歉，请输入数字！");
            return str;
        }
    }

    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];

        for(int i = 0; i < number.length(); ++i) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }

        return array;
    }

    public static String getChineseInteger(int[] integers, boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        } else {
            for(int i = 0; i < length; ++i) {
                String key = "";
                if (integers[i] == 0) {
                    if (length - i == 13) {
                        key = IUNIT[4];
                    } else if (length - i == 9) {
                        key = IUNIT[8];
                    } else if (length - i == 5 && isWan) {
                        key = IUNIT[4];
                    } else if (length - i == 1) {
                        key = IUNIT[0];
                    }

                    if (length - i > 1 && integers[i + 1] != 0) {
                        key = key + NUMBERS[0];
                    }
                }

                chineseInteger.append(integers[i] == 0 ? key : NUMBERS[integers[i]] + IUNIT[length - i - 1]);
            }

            return chineseInteger.toString();
        }
    }

    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");

        for(int i = 0; i < decimals.length && i != 3; ++i) {
            chineseDecimal.append(decimals[i] == 0 ? "" : NUMBERS[decimals[i]] + DUNIT[i]);
        }

        return chineseDecimal.toString();
    }

    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }

            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String number = "12.56";
        System.out.println(number + ": " + toChinese(number));
        number = "1234567890563886.123";
        System.out.println(number + ": " + toChinese(number));
        number = "1600";
        System.out.println(number + ": " + toChinese(number));
        number = "156,0";
        System.out.println(number + ": " + toChinese(number));
        number = "-156,0";
        System.out.println(number + ": " + toChinese(number));
        number = "0.12";
        System.out.println(number + ": " + toChinese(number));
        number = "0.0";
        System.out.println(number + ": " + toChinese(number));
        number = "01.12";
        System.out.println(number + ": " + toChinese(number));
        number = "0125";
        System.out.println(number + ": " + toChinese(number));
        number = "-0125";
        System.out.println(number + ": " + toChinese(number));
        number = "sdw5655";
        System.out.println(number + ": " + toChinese(number));
        System.out.println(null + ": " + toChinese((String)null));
    }
}
