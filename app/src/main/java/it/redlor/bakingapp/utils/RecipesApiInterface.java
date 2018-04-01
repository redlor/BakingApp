package it.redlor.bakingapp.utils;

import java.util.List;

import io.reactivex.Observable;
import it.redlor.bakingapp.pojos.Recipe;
import retrofit2.http.GET;

public interface RecipesApiInterface {

   // @Streaming
    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipes();
}
