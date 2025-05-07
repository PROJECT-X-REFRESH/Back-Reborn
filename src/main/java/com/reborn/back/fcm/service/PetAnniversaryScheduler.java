package com.reborn.back.fcm.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.fcm.dto.FcmRequestDto;
import com.reborn.back.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetAnniversaryScheduler {

    private final UserRepository userRepository;
    private final FcmService fcmService;

    /**
     * 매일 오전 9시에 실행 (Asia/Seoul)
     * - 오늘이 birth 월/일 → 생일
     * - 오늘이 death 월/일 → 기일
     */
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void sendPetAnniversaryNotifications() {
        LocalDate today = LocalDate.now();
        userRepository.findAllByPetListIsNotEmpty()
                .forEach(this::processUser);
    }

    private void processUser(User user) {
        String token = user.getDeviceToken();
        if (token == null || token.isBlank()) {
            log.warn("User({})의 deviceToken이 없어 알림을 건너뜁니다.", user.getUid());
            return;
        }

        LocalDate today = LocalDate.now();

        // 2) 이 유저의 각 펫을 돌며 anniversary 체크
        user.getPetList().stream()
                .filter(pet -> isAnniversary(pet, today))
                .forEach(pet -> sendForPet(user, pet, today));
    }

    private boolean isAnniversary(Pet pet, LocalDate date) {
        return (pet.getBirth() != null && sameMonthDay(pet.getBirth(), date))
                || (pet.getDeath() != null && sameMonthDay(pet.getDeath(), date));
    }

    private boolean sameMonthDay(LocalDate d1, LocalDate d2) {
        return d1.getMonth() == d2.getMonth()
                && d1.getDayOfMonth() == d2.getDayOfMonth();
    }

    private void sendForPet(User user, Pet pet, LocalDate date) {
        String token = user.getDeviceToken();

        boolean isBirthday = pet.getBirth() != null && sameMonthDay(pet.getBirth(), date);
        String title = isBirthday
                ? "🎂 " + pet.getName() + "의 생일이에요!"
                : "🕯️ " + pet.getName() + "의 기일입니다";
        String body = isBirthday
                ? pet.getName() + "의 생일을 함께 축하해주세요!"
                : pet.getName() + "의 기일을 기억해 주세요.";

        try {
            FcmRequestDto req = FcmRequestDto.builder()
                    .token(token)
                    .title(title)
                    .body(body)
                    .build();
            fcmService.sendMessage(req);
            log.info("Pet({}) 알림 전송 완료 → userId={}, token={}",
                    pet.getId(), user.getUid(), token);
        } catch (Exception e) {
            log.error("Pet({}) 알림 전송 중 오류 발생: {}", pet.getId(), e.getMessage(), e);
        }
    }
}
