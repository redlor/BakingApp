package it.redlor.bakingapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Ingredient object
 */

public class Ingredient implements Parcelable {

    public static final Parcelable.Creator<Ingredient> CREATOR =
            new Parcelable.Creator<Ingredient>() {
                public Ingredient createFromParcel(Parcel in) {
                    return new Ingredient(in);
                }

                public Ingredient[] newArray(int size) {
                    return new Ingredient[size];
                }
            };
    @SerializedName("quantity")
    @Expose
    private float quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    private Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    @Override
    public String toString() {
        return "Ingredients:{" +
                "quantity='" + quantity + '\'' +
                ", measure='" + measure + '\'' +
                ", ingredient'" + ingredient + '\'' +
                '}';
    }
}
