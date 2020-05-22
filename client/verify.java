// package client;

// public class verify {
// private Boolean ipVerify(String ip) {
// String ipReg =
// "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
// Pattern ipPattern = Pattern.compile(ipReg);
// Matcher ipVerify = ipPattern.matcher(ip);
// if (ipVerify.matches()) {
// return true;
// } else {
// return false;
// }
// }

// private Boolean portVerify(String port) {
// int newPort = Integer.parseInt(port);
// if (newPort > 65535 || newPort < 0) {
// return false;
// } else {
// return true;
// }
// }

// private Boolean udpVerify(String udpInput) {
// Boolean udpStatus = Boolean.valueOf(udpInput);
// if (udpStatus) {
// return true;
// } else {
// return false;
// }
// }
// }