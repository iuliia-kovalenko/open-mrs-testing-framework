package requests.skeleton.interfaces;

import java.util.Map;

// Поиск: только GET с параметрами — /patient?identifier=..., /visit?patient=...
public interface SearchEndpointInterface {
    Object search(Map<String, ?> params);
}
