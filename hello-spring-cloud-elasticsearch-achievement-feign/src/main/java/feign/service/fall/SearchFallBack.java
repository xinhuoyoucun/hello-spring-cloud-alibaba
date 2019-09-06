package feign.service.fall;

import feign.service.AchievementService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author by laiyuan
 * @Date 2019/9/4 15:03
 * @Description: TODO
 * @Version 1.0
 */
@Component
public class SearchFallBack implements AchievementService {
    @Override
    public List<String> suggest(String text) {
        return null;
    }

    @Override
    public Map<String, Object> search(String text, int index, int pageSize) {
        return null;
    }
}
