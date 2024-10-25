package kr.kro.gonggibap.core.util;

import kr.kro.gonggibap.core.constant.Adjective;
import kr.kro.gonggibap.core.constant.Food;
import org.springframework.stereotype.Component;

import java.util.Random;

public class RandomNicknameGenerator {
    // 형용사와 음식을 조합하여 랜덤 닉네임 생성
    public static String generateRandomNickname() {
        String adjective = getRandomAdjective();
        String food = getRandomFood();
        return adjective + " " + food;
    }

        // 랜덤으로 형용사를 선택
    private static String getRandomAdjective() {
        Adjective[] adjectives = Adjective.values();
        Random random = new Random();
        int randomIndex = random.nextInt(adjectives.length);
        return adjectives[randomIndex].name();
    }

    // 랜덤으로 음식을 선택
    private static String getRandomFood() {
        Food[] foods = Food.values();
        Random random = new Random();
        int randomIndex = random.nextInt(foods.length);
        return foods[randomIndex].name();
    }

}
