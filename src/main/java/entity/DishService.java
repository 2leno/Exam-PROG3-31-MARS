package entity;

import dto.DishCreationDto;
import exception.BadRequestException;
import repository.DishRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> createDishes(List<DishCreationDto> dishCreationDTOs) {
        for (DishCreationDto dto : dishCreationDTOs) {
            Optional<Dish> existingDish = dishRepository.findByName(dto.getName());
            if (existingDish.isPresent()) {
                throw new BadRequestException("Dish.name=" + dto.getName() + " already exists");
            }
        }

        List<Dish> dishesToCreate = dishCreationDTOs.stream()
            .map(dto -> {
                Dish dish = new Dish();
                dish.setName(dto.getName());
                dish.setCategory(dto.getCategory());
                dish.setPrice(dto.getPrice());
                return dish;
            })
            .collect(Collectors.toList());

        return dishRepository.saveAll(dishesToCreate);
    }

    public List<Dish> getDishesWithFilters(BigDecimal priceUnder, BigDecimal priceOver, String name) {
        return dishRepository.findByFilters(priceUnder, priceOver, name);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish updateDish(Dish dish) {
        return dishRepository.update(dish);
    }
}