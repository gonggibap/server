package kr.kro.gonggibap.core.config.jwt.constant;

public enum TokenType {
    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType){
        return TokenType.ACCESS.name().equals(tokenType);
    }

}
