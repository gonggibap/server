package kr.kro.gonggibap.domain.history.service;

import kr.kro.gonggibap.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
}
