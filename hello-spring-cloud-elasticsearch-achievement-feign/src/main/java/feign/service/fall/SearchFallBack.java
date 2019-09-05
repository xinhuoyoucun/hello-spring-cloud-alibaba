package feign.service.fall;

import feign.service.AchievementService;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
