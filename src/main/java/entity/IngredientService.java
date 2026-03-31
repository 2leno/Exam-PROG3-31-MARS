package entity;

import exception.BadRequestException;
import repository.IngredientRepository;

import java.time.Instant;
import java.util.List;

public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientValidator ingredientValidator;

    public IngredientService(IngredientRepository ingredientRepository,
                             IngredientValidator ingredientValidator) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientValidator = ingredientValidator;
    }

    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient getById(int id) {
        return ingredientRepository.findById(id);
    }

    public StockValue getStockAt(int id, String at, String unit) throws BadRequestException {
        ingredientValidator.validateStockParams(at, unit);
        Ingredient ingredient = ingredientRepository.findById(id);
        if (ingredient == null) {
            return null;
        }
        ingredient.setStockMovementList(ingredientRepository.findStockMovements(id));
        Instant instant = Instant.parse(at);
        UnitEnum unitEnum = UnitEnum.valueOf(unit.toUpperCase());
        StockValue sv = ingredient.getStockValueAt(instant);
        sv.setUnit(unitEnum);
        return sv;
    }
}
