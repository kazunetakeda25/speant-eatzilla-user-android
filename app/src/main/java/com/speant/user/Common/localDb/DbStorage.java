package com.speant.user.Common.localDb;

import android.database.Cursor;
import android.util.Log;

import com.speant.user.Common.CONST;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.AdapterRefreshCallBack;
import com.speant.user.Common.callBacks.onCartUpdate;
import com.speant.user.Models.AddOns;
import com.speant.user.Models.FinalFoodList;
import com.speant.user.Models.FoodQuantity;
import com.speant.user.Models.Pivot;
import com.speant.user.Models.RestaurantData;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.speant.user.Common.CONST.ADD_DATA;
import static com.speant.user.Common.CONST.N0_ADDON;
import static com.speant.user.Common.CONST.NO_QUANTITY_ID;
import static com.speant.user.Common.CONST.REDUCE_DATA;
import static org.greenrobot.eventbus.EventBus.TAG;

public class DbStorage {

    List<AddOns> selectedAddOnsList;
    FinalFoodList finalFoodList;
    AddOnDb addOnDb;
    GroupDb addOnGroupDb;
    QuantityDb quantityDb;
    String ACTION_TYPE;
    double totalAmount;
    FoodQuantity foodQuantity;
    private CartDetailsDb cartDetailsDb;
    private List<CartDetailsDb> cartDetailsDbList;
    private String restaurant_id = " ";
    private FoodItemDb foodItemDb;
    RestaurantData restaurantData;
    AdapterRefreshCallBack adapterRefreshCallBack;
    onCartUpdate oncartUpdate;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("##.00",symbols);

    public void setAddOnData(List<AddOns> selectedAddOnsList, FoodQuantity foodQuantity, RestaurantData restaurantData, FinalFoodList finalFoodList, String ACTION_TYPE, AdapterRefreshCallBack adapterRefreshCallBack) {
        this.adapterRefreshCallBack = adapterRefreshCallBack;
        this.selectedAddOnsList = selectedAddOnsList;
        this.foodQuantity = foodQuantity;
        this.finalFoodList = finalFoodList;
        this.ACTION_TYPE = ACTION_TYPE;
        this.restaurantData = restaurantData;
        totalAmount = 0.0;
        intialiseRestIdDB();

        if (selectedAddOnsList != null && foodQuantity != null) {
            Log.e(TAG, "setAddOnData: both are not null");
            for (AddOns addOns : selectedAddOnsList) {
                setAddOnDbDataQuery(addOns, getAddonDbQuery(addOns, finalFoodList.getFood_id()));
                double addOnPrice = Double.parseDouble(addOns.getPrice());
                totalAmount += addOnPrice;
            }

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < selectedAddOnsList.size(); i++) {
                if (i == selectedAddOnsList.size() - 1) {
                    str.append(selectedAddOnsList.get(i).getId());
                } else {
                    str.append(selectedAddOnsList.get(i).getId());
                    str.append(",");
                }
            }

            setQuantityDbDataQuery(foodQuantity, getQuantityDbQuery(foodQuantity, finalFoodList.getFood_id()));
            totalAmount += Double.parseDouble(foodQuantity.getPivot().getPrice());
            Log.e(TAG, "setAddOnData:totalAmount " + totalAmount);
            setGroupDb(str.toString(), foodQuantity, getGroupDbQuery(str.toString(), finalFoodList.getFood_id(), foodQuantity.getId()), ACTION_TYPE);
        } else if (selectedAddOnsList == null && foodQuantity != null) {
            Log.e(TAG, "setAddOnData: selectedAddOnsList is null");
            setQuantityDbDataQuery(foodQuantity, getQuantityDbQuery(foodQuantity, finalFoodList.getFood_id()));
            totalAmount += Double.parseDouble(foodQuantity.getPivot().getPrice());
            Log.e(TAG, "setAddOnData:totalAmount " + totalAmount);
            setGroupDb(N0_ADDON, foodQuantity, getGroupDbQuery(N0_ADDON, finalFoodList.getFood_id(), foodQuantity.getId()), ACTION_TYPE);
        } else if (selectedAddOnsList != null) {
            Log.e(TAG, "setAddOnData: foodQuantity is null");
            for (AddOns addOns : selectedAddOnsList) {
                setAddOnDbDataQuery(addOns, getAddonDbQuery(addOns, finalFoodList.getFood_id()));
                double addOnPrice = Double.parseDouble(addOns.getPrice());
                totalAmount += addOnPrice;
            }

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < selectedAddOnsList.size(); i++) {
                if (i == selectedAddOnsList.size() - 1) {
                    str.append(selectedAddOnsList.get(i).getId());
                } else {
                    str.append(selectedAddOnsList.get(i).getId());
                    str.append(",");
                }
            }

            FoodQuantity foodQuatiy = new FoodQuantity();
            foodQuatiy.setId(NO_QUANTITY_ID);
            foodQuatiy.setName("No Quantity");
            Pivot pivot = new Pivot();
            pivot.setPrice(finalFoodList.getPrice());
            foodQuatiy.setPivot(pivot);
            this.foodQuantity = foodQuatiy;
            setQuantityDbDataQuery(foodQuatiy, getQuantityDbQuery(foodQuatiy, finalFoodList.getFood_id()));

            totalAmount += Double.parseDouble(foodQuatiy.getPivot().getPrice());
            Log.e(TAG, "setAddOnData:totalAmount " + totalAmount);
            setGroupDb(str.toString(), foodQuatiy, getGroupDbQuery(str.toString(), finalFoodList.getFood_id(), foodQuatiy.getId()), ACTION_TYPE);
        } else {
            Log.e(TAG, "setAddOnData: both are null");
            FoodQuantity foodQuatiy = new FoodQuantity();
            foodQuatiy.setId(NO_QUANTITY_ID);
            foodQuatiy.setName("No Quantity");
            Pivot pivot = new Pivot();
            pivot.setPrice(finalFoodList.getPrice());
            foodQuatiy.setPivot(pivot);
            this.foodQuantity = foodQuatiy;
            setQuantityDbDataQuery(foodQuatiy, getQuantityDbQuery(foodQuatiy, finalFoodList.getFood_id()));

            totalAmount += Double.parseDouble(foodQuatiy.getPivot().getPrice());
            setGroupDb(N0_ADDON, foodQuatiy, getGroupDbQuery(N0_ADDON, finalFoodList.getFood_id(), foodQuatiy.getId()), ACTION_TYPE);
        }

    }

    public void setAddOnCartData(GroupDb groupDb, List<FoodItemDb> foodItemDb, List<QuantityDb> quantityDb, String ACTION_TYPE, onCartUpdate oncartUpdate) {
        intialiseRestIdDB();
        FoodQuantity foodQuatiy = new FoodQuantity();

        if(quantityDb.size()>0) {
            foodQuatiy.setId(quantityDb.get(0).getQuantity_id());
            foodQuatiy.setName(quantityDb.get(0).getName());
            Pivot pivot = new Pivot();
            pivot.setPrice(quantityDb.get(0).getPrice());
            foodQuatiy.setPivot(pivot);
        }else{
            foodQuatiy.setId(NO_QUANTITY_ID);
            foodQuatiy.setName("No Quantity");
            Pivot pivot = new Pivot();
            pivot.setPrice(String.valueOf(foodItemDb.get(0).getFood_cost()));
            foodQuatiy.setPivot(pivot);
        }

        FinalFoodList finalFoodListSingle = new FinalFoodList();
        finalFoodListSingle.setPrice(String.valueOf(foodItemDb.get(0).getFood_cost()));
        finalFoodListSingle.setFood_id(foodItemDb.get(0).getFood_id());
        finalFoodListSingle.setName(foodItemDb.get(0).getFood_name());
        finalFoodListSingle.setItem_count(foodItemDb.get(0).getFood_qty());
        finalFoodListSingle.setIs_veg(foodItemDb.get(0).getFood_type());
        finalFoodListSingle.setDescription(foodItemDb.get(0).getFood_desc());
        finalFoodListSingle.setItem_tax(foodItemDb.get(0).getFood_tax());
        finalFoodListSingle.setDiscount_type(foodItemDb.get(0).getDiscount_type());
        finalFoodListSingle.setOffer_amount(""+foodItemDb.get(0).getOffer_amount());
        finalFoodListSingle.setTarget_amount(""+foodItemDb.get(0).getTarget_amount());

        this.finalFoodList = finalFoodListSingle;
        this.foodQuantity = foodQuatiy;
        this.oncartUpdate = oncartUpdate;
        this.ACTION_TYPE = ACTION_TYPE;
        this.restaurantData = getRestaurantDetails();
        totalAmount = 0.0;

        totalAmount += Double.parseDouble(foodQuatiy.getPivot().getPrice());

//        setGroupDb(groupDb.getGroupData(), foodQuantity, getGroupDbQuery(groupDb.getGroupData(), finalFoodList.getFood_id(), foodQuantity.getId()), ACTION_TYPE);
        setGroupDbOnCart(getGroupDbQuery(groupDb.getGroupData(), finalFoodList.getFood_id(), foodQuantity.getId()), ACTION_TYPE);
    }


    private ArrayList<QuantityDb> getQuantityDbQuery(FoodQuantity foodQuantity, String foodId) {

        String SQL_SELECTED_ADD_ON = "SELECT * FROM " + QuantityDbDao.TABLENAME + " WHERE " +
                QuantityDbDao.Properties.Food_id.columnName + "=" + foodId + " AND "
                + QuantityDbDao.Properties.Quantity_id.columnName + "=";

        Cursor cursor = App.getInstance().getmDaoSession().getQuantityDbDao().getDatabase().rawQuery(SQL_SELECTED_ADD_ON + foodQuantity.getId(), null);

        return getQuantityDbResult(cursor);
    }

    private ArrayList<QuantityDb> getQuantityDbResult(Cursor c) {
        //get Cart Count of preferred productId
        ArrayList<QuantityDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(QuantityDbDao.Properties.Food_id.columnName);
        final int quanity_id = c.getColumnIndex(QuantityDbDao.Properties.Id.columnName);
        final int quanity_dbid = c.getColumnIndex(QuantityDbDao.Properties.Quantity_id.columnName);
        final int quanity_name = c.getColumnIndex(QuantityDbDao.Properties.Name.columnName);
        final int rest_id = c.getColumnIndex(QuantityDbDao.Properties.Restaurant_id.columnName);
        final int quanity_price = c.getColumnIndex(QuantityDbDao.Properties.Price.columnName);

        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String quanity_id_fin = c.getString(quanity_id);
                        final String quanity_dbid_fin = c.getString(quanity_dbid);
                        final String quanity_name_fin = c.getString(quanity_name);
                        final String quanity_price_fin = c.getString(quanity_price);

                        QuantityDb quantityDb = new QuantityDb();
                        quantityDb.setRestaurant_id(CONST.restaren_id);
                        quantityDb.setFood_id(food_id_fin);
                        quantityDb.setQuantity_id(quanity_dbid_fin);
                        quantityDb.setName(quanity_name_fin);
                        quantityDb.setPrice(quanity_price_fin);
                        quantityDb.setId(Long.valueOf(quanity_id_fin));
                        result.add(quantityDb);
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    private void setQuantityDbDataQuery(FoodQuantity foodQuantity, ArrayList<QuantityDb> result) {
        if (result.size() > 0) {
            for (QuantityDb quantityDbSingle : result) {
                quantityDb = quantityDbSingle;
                Log.e("TAG", "onProductDetailsFragmentView:result quantityDb.getFood_id " + quantityDb.getFood_id());
                Log.e("TAG", "onProductDetailsFragmentView:result quantityDb.getName()" + quantityDb.getName());
                App.getInstance().getmDaoSession().getQuantityDbDao().update(quantityDb);
            }
        } else {
            quantityDb = new QuantityDb();
            quantityDb.setRestaurant_id(CONST.restaren_id);
            quantityDb.setFood_id(finalFoodList.getFood_id());
            quantityDb.setQuantity_id(foodQuantity.getId());
            quantityDb.setName(foodQuantity.getName());
            quantityDb.setPrice(foodQuantity.getPivot().getPrice());
            quantityDb.setId(null);
            App.getInstance().getmDaoSession().getQuantityDbDao().insert(quantityDb);
        }
    }


    private ArrayList<AddOnDb> getAddonDbQuery(AddOns addOns, String foodId) {

        String SQL_SELECTED_ADD_ON = "SELECT * FROM " + AddOnDbDao.TABLENAME + " WHERE " + AddOnDbDao.Properties.Food_id.columnName + "=" + foodId + " AND "
                + AddOnDbDao.Properties.Addon_id.columnName + "=";

        Cursor cursor = App.getInstance().getmDaoSession().getAddOnDbDao().getDatabase().rawQuery(SQL_SELECTED_ADD_ON + addOns.getId(), null);

        return getAddonDbResult(cursor);
    }

    private ArrayList<AddOnDb> getAddonDbResult(Cursor c) {
        //get Cart Count of preferred productId
        ArrayList<AddOnDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(AddOnDbDao.Properties.Food_id.columnName);
        final int add_on_id = c.getColumnIndex(AddOnDbDao.Properties.Id.columnName);
        final int add_on_dbid = c.getColumnIndex(AddOnDbDao.Properties.Addon_id.columnName);
        final int add_on_name = c.getColumnIndex(AddOnDbDao.Properties.Name.columnName);
        final int rest_id = c.getColumnIndex(AddOnDbDao.Properties.Restaurant_id.columnName);
        final int add_on_price = c.getColumnIndex(AddOnDbDao.Properties.Price.columnName);

        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final Long add_on_id_fin = Long.valueOf(c.getString(add_on_id));
                        final String add_on_dbid_fin = c.getString(add_on_dbid);
                        final String add_on_name_fin = c.getString(add_on_name);
                        final String add_on_price_fin = c.getString(add_on_price);

                        AddOnDb addOnDblocal = new AddOnDb();
                        addOnDblocal.setRestaurant_id(CONST.restaren_id);
                        addOnDblocal.setFood_id(food_id_fin);
                        addOnDblocal.setAddon_id(add_on_dbid_fin);
                        addOnDblocal.setName(add_on_name_fin);
                        addOnDblocal.setId(add_on_id_fin);
                        addOnDblocal.setPrice(add_on_price_fin);
                        result.add(addOnDblocal);
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    private ArrayList<GroupDb> getGroupDbQuery(String groupData, String foodId, String quantityId) {
        String SQL_GROUP_ADD_ON = "SELECT * FROM " + GroupDbDao.TABLENAME + " WHERE "
                + GroupDbDao.Properties.Food_id.columnName + "=" + foodId + " AND "
                + GroupDbDao.Properties.GroupData.columnName + "='" + groupData + "' AND "
                + GroupDbDao.Properties.Quantity_id.columnName + "=" + quantityId;

        Cursor cursor = App.getInstance().getmDaoSession().getGroupDbDao().getDatabase().rawQuery(SQL_GROUP_ADD_ON, null);

        return getGroupDbResult(cursor);

    }
    public ArrayList<GroupDb> getSingleGroupDbQuery(String foodId) {
        String SQL_GROUP_ADD_ON = "SELECT * FROM " + GroupDbDao.TABLENAME + " WHERE "
                + GroupDbDao.Properties.Food_id.columnName + "=" + foodId;

        Cursor cursor = App.getInstance().getmDaoSession().getGroupDbDao().getDatabase().rawQuery(SQL_GROUP_ADD_ON, null);

        return getGroupDbResult(cursor);
    }

    private ArrayList<GroupDb> getGroupDbResult(Cursor c) {
        //get Cart Count of preferred productId
        ArrayList<GroupDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(GroupDbDao.Properties.Food_id.columnName);
        final int group_id = c.getColumnIndex(GroupDbDao.Properties.Id.columnName);
        final int group_data = c.getColumnIndex(GroupDbDao.Properties.GroupData.columnName);
        final int group_cout = c.getColumnIndex(GroupDbDao.Properties.GroupCount.columnName);
        final int rest_id = c.getColumnIndex(GroupDbDao.Properties.Restaurant_id.columnName);
        final int group_amount = c.getColumnIndex(GroupDbDao.Properties.Groupamount.columnName);
        final int quantity_id = c.getColumnIndex(GroupDbDao.Properties.Quantity_id.columnName);

        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final long group_id_fin = Long.parseLong(c.getString(group_id));
                        final String group_data_fin = c.getString(group_data);
                        final String quantity_fin = c.getString(quantity_id);
                        final int group_cout_fin = Integer.parseInt(c.getString(group_cout));
                        final double group_amount_fin = Double.parseDouble(c.getString(group_amount));

                        GroupDb groupDb = new GroupDb();
                        groupDb.setRestaurant_id(CONST.restaren_id);
                        groupDb.setFood_id(food_id_fin);
                        groupDb.setGroupData(group_data_fin);
                        groupDb.setGroupCount(group_cout_fin);
                        groupDb.setGroupamount(group_amount_fin);
                        groupDb.setId(group_id_fin);
                        groupDb.setQuantity_id(quantity_fin);
                        result.add(groupDb);
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    public ArrayList<GroupDb> isHavingAddOnsInDbQuery(FinalFoodList pojo) {

        String SQL_ADD_ON_DATA = "SELECT * FROM " + GroupDbDao.TABLENAME + " WHERE " + GroupDbDao.Properties.Food_id.columnName + "=";

        Cursor cursor = App.getInstance().getmDaoSession().getGroupDbDao().getDatabase().rawQuery(SQL_ADD_ON_DATA + pojo.getFood_id(), null);

        return getGroupDbResult(cursor);

    }

    private void setGroupDb(String groupData, FoodQuantity foodQuantity, ArrayList<GroupDb> result, String ACTION_TYPE) {
        if (result.size() > 0) {
            for (GroupDb addOnGroupDbSingle : result) {
                addOnGroupDb = addOnGroupDbSingle;

                Log.e("TAG", "onProductDetails addOnGroupDb count before" + addOnGroupDb.getGroupCount());

                switch (ACTION_TYPE) {
                    case ADD_DATA:
                        addOnGroupDb.setGroupCount(addOnGroupDb.getGroupCount() + 1);
                        break;
                    case REDUCE_DATA:
                        addOnGroupDb.setGroupCount(addOnGroupDb.getGroupCount() - 1);
                        break;
                }

                Log.e("TAG", "onProductDetails addOnGroupDb count after" + addOnGroupDb.getGroupCount());
                Log.e("TAG", "onProductDetails addOnGroupDb " + addOnGroupDb.getFood_id());
                Log.e("TAG", "onProductDetails addOnGroupDb" + addOnGroupDb.getGroupData());

                if (addOnGroupDb.getGroupCount() <= 0) {
                    App.getInstance().getmDaoSession().getGroupDbDao().delete(addOnGroupDb);
                } else {
                    double finalAmount = Double.parseDouble(decimalFormat.format(addOnGroupDb.getGroupCount() * totalAmount));
                    addOnGroupDb.setGroupamount(finalAmount);
                    Log.e("TAG", "onProductDetails addOnGroupDb amount after" + addOnGroupDb.getGroupamount());
                    App.getInstance().getmDaoSession().getGroupDbDao().update(addOnGroupDb);

                    //add to lastUpdated table on updating a data in addOnGroup
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().insert(setLastAddedDb(addOnGroupDb));


                }
            }
            //add data to FoodItemDb
            updateTotal();
        } else {
            addOnGroupDb = new GroupDb();
            addOnGroupDb.setRestaurant_id(CONST.restaren_id);
            addOnGroupDb.setFood_id(finalFoodList.getFood_id());
            addOnGroupDb.setGroupData(groupData);
            addOnGroupDb.setGroupCount(1);
            addOnGroupDb.setIs_veg(finalFoodList.getIs_veg());
            addOnGroupDb.setGroupamount(totalAmount);
            addOnGroupDb.setQuantity_id(foodQuantity.getId());
            App.getInstance().getmDaoSession().getGroupDbDao().insert(addOnGroupDb);

            //add to lastUpdated table on updating a data in addOnGroup
            App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
            App.getInstance().getmDaoSession().getGroupLastAddedDbDao().insert(setLastAddedDb(addOnGroupDb));

            //add data to FoodItemDb
            updateTotal();
        }
    }

    private void setGroupDbOnCart(ArrayList<GroupDb> result, String ACTION_TYPE) {
        if (result.size() > 0) {
            for (GroupDb addOnGroupDbSingle : result) {
                addOnGroupDb = addOnGroupDbSingle;

                Log.e("TAG", "setGroupDbOnCart addOnGroupDb count before" + addOnGroupDb.getGroupCount());
                Log.e("TAG", "setGroupDbOnCart addOnGroupDb amount before" + addOnGroupDb.getGroupamount());

                switch (ACTION_TYPE) {
                    case ADD_DATA:
                        String CurrentLang = Locale.getDefault().getLanguage();
                        Log.e(TAG, "setGroupDbOnCart:CurrentLang "+CurrentLang );
                        double finalAmountPlus = Double.parseDouble(decimalFormat.format(addOnGroupDb.getGroupamount() + (addOnGroupDb.getGroupamount()/addOnGroupDb.getGroupCount())));
                        addOnGroupDb.setGroupamount(finalAmountPlus);
                        addOnGroupDb.setGroupCount(addOnGroupDb.getGroupCount() + 1);

                        break;
                    case REDUCE_DATA:
                        double finalAmountMinus = Double.parseDouble(decimalFormat.format(addOnGroupDb.getGroupamount() - (addOnGroupDb.getGroupamount()/addOnGroupDb.getGroupCount())));
                        addOnGroupDb.setGroupamount(finalAmountMinus);
                        addOnGroupDb.setGroupCount(addOnGroupDb.getGroupCount() - 1);
                        break;
                }

                Log.e("TAG", "setGroupDbOnCart addOnGroupDb count after" + addOnGroupDb.getGroupCount());
                Log.e("TAG", "setGroupDbOnCart addOnGroupDb amount after" + addOnGroupDb.getGroupamount());

                if (addOnGroupDb.getGroupCount() <= 0) {
                    App.getInstance().getmDaoSession().getGroupDbDao().delete(addOnGroupDb);
                } else {
                    App.getInstance().getmDaoSession().getGroupDbDao().update(addOnGroupDb);

                    List<GroupDb> groupDbList = App.getInstance().getmDaoSession().getGroupDbDao().loadAll();
                    for (GroupDb groupDb : groupDbList) {
                        Log.e(TAG, "setGroupDbOnCart:groupDb final "+groupDb.getGroupamount() );
                        Log.e(TAG, "setGroupDbOnCart:groupDb final "+groupDb.getGroupCount() );
                        Log.e(TAG, "setGroupDbOnCart:groupDb final "+groupDb.getId() );
                    }

                    //add to lastUpdated table on updating a data in addOnGroup
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().insert(setLastAddedDb(addOnGroupDb));


                }
            }
            //add data to FoodItemDb
            updateTotal();
        }
    }

    private void updateTotal() {
        switch (ACTION_TYPE) {
            case ADD_DATA:
                AddFoodToDb();
                break;
            case REDUCE_DATA:
                ReduceFoodInDb();
                break;
        }
    }

    private GroupLastAddedDb setLastAddedDb(GroupDb groupDb) {
        GroupLastAddedDb groupLastAddedDb = new GroupLastAddedDb();
        groupLastAddedDb.setRestaurant_id(CONST.restaren_id);
        groupLastAddedDb.setFood_id(finalFoodList.getFood_id());
        groupLastAddedDb.setGroupData(groupDb.getGroupData());
        groupLastAddedDb.setGroupCount(groupDb.getGroupCount());
        groupLastAddedDb.setGroupCount(groupDb.getGroupCount());
        return groupLastAddedDb;
    }

    private void setAddOnDbDataQuery(AddOns addOns, ArrayList<AddOnDb> result) {

        if (result.size() > 0) {
            for (AddOnDb addOnDbSingle : result) {
                addOnDb = addOnDbSingle;
                Log.e("TAG", "onProductDetailsFragmentView:result addOnDb.getFood_id " + addOnDb.getFood_id());
                Log.e("TAG", "onProductDetailsFragmentView:result addOnDb.getName" + addOnDb.getName());
                App.getInstance().getmDaoSession().getAddOnDbDao().update(addOnDb);
            }
        } else {
            addOnDb = new AddOnDb();
            addOnDb.setRestaurant_id(CONST.restaren_id);
            addOnDb.setFood_id(finalFoodList.getFood_id());
            addOnDb.setAddon_id(addOns.getId());
            addOnDb.setName(addOns.getName());
            addOnDb.setPrice(addOns.getPrice());
            App.getInstance().getmDaoSession().getAddOnDbDao().insert(addOnDb);
        }
    }

    public List<FoodItemDb> getAddedFoodDetailsQuery(String food_id) {
        String SQL_SINGLE_DATA = "SELECT * FROM " + FoodItemDbDao.TABLENAME + " WHERE " + FoodItemDbDao.Properties.Food_id.columnName + "=";

        Cursor cursor = App.getInstance().getmDaoSession().getFoodItemDbDao().getDatabase().rawQuery(SQL_SINGLE_DATA + food_id, null);

        return getAddedFoodDetailsResult(cursor);
    }

    private List<FoodItemDb> getAddedFoodDetailsResult(Cursor c) {
        //get Cart Count of preferred productId
        ArrayList<FoodItemDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(FoodItemDbDao.Properties.Food_id.columnName);
        final int food_cost = c.getColumnIndex(FoodItemDbDao.Properties.Food_cost.columnName);
        final int food_name = c.getColumnIndex(FoodItemDbDao.Properties.Food_name.columnName);
        final int food_count = c.getColumnIndex(FoodItemDbDao.Properties.Food_qty.columnName);
        final int food_type = c.getColumnIndex(FoodItemDbDao.Properties.Food_type.columnName);
        final int food_desc = c.getColumnIndex(FoodItemDbDao.Properties.Food_desc.columnName);
        final int food_tax = c.getColumnIndex(FoodItemDbDao.Properties.Food_tax.columnName);
        final int rest_id = c.getColumnIndex(FoodItemDbDao.Properties.Restaurant_id.columnName);
        final int target_amount = c.getColumnIndex(FoodItemDbDao.Properties.Target_amount.columnName);
        final int offer_amount = c.getColumnIndex(FoodItemDbDao.Properties.Offer_amount.columnName);
        final int discount_type = c.getColumnIndex(FoodItemDbDao.Properties.Discount_type.columnName);
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String food_name_fin = c.getString(food_name);
                        final String food_desc_fin = c.getString(food_desc);
                        final double food_cost_fin = Double.parseDouble(c.getString(food_cost));
                        final int food_count_fin = Integer.parseInt(c.getString(food_count));
                        final int food_type_fin = Integer.parseInt(c.getString(food_type));
                        final double food_tax_fin = Double.parseDouble(c.getString(food_tax));
                        final String target_amount_fin = c.getString(target_amount);
                        final String offer_amount_fin = c.getString(offer_amount);
                        final String discount_type_fin = c.getString(discount_type);
                        result.add(new FoodItemDb(rest_id_fin, food_id_fin, food_count_fin, food_name_fin, food_type_fin, food_cost_fin, food_desc_fin,food_tax_fin,discount_type_fin,target_amount_fin,offer_amount_fin));
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        return result;
    }

    public List<AddOnDb> getAddedAddOnDetailsQuery(String groupData, String foodId) {
        List<AddOnDb> addOnDbList = new ArrayList<>();
        String[] addOnIds = groupData.split(",");
        for (String addOnId : addOnIds) {
            String SQL_ADD_ON = "SELECT * FROM " + AddOnDbDao.TABLENAME + " WHERE "
                    + AddOnDbDao.Properties.Food_id.columnName + "=" + foodId + " AND "
                    + AddOnDbDao.Properties.Addon_id.columnName + "='" + addOnId+"'";

            Cursor cursor = App.getInstance().getmDaoSession().getAddOnDbDao().getDatabase().rawQuery(SQL_ADD_ON, null);
            List<AddOnDb> addOnDbListTemp = getAddedAddOnDetailsResult(cursor);
            if(addOnDbListTemp.size() > 0){
                    addOnDbList.addAll(addOnDbListTemp);
            }else{
                Log.e(TAG, "getAddedAddOnDetailsQuery:Sizee is Zero " );
            }
        }
        return addOnDbList;
    }

    private List<AddOnDb> getAddedAddOnDetailsResult(Cursor c) {

        ArrayList<AddOnDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(AddOnDbDao.Properties.Food_id.columnName);
        final int addOn_id = c.getColumnIndex(AddOnDbDao.Properties.Addon_id.columnName);
        final int price = c.getColumnIndex(AddOnDbDao.Properties.Price.columnName);
        final int name = c.getColumnIndex(AddOnDbDao.Properties.Name.columnName);
        final int id = c.getColumnIndex(AddOnDbDao.Properties.Id.columnName);
        final int rest_id = c.getColumnIndex(AddOnDbDao.Properties.Restaurant_id.columnName);
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String addOn_id_fin = c.getString(addOn_id);
                        final String price_fin = c.getString(price);
                        final String name_fin = c.getString(name);
                        final long id_fin = Long.parseLong(c.getString(id));

                        result.add(new AddOnDb(id_fin, food_id_fin, addOn_id_fin, rest_id_fin, name_fin, price_fin));
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        return result;
    }

    public List<QuantityDb> getAddedQuantityDetailsQuery(String quantity_id, String foodId) {
        String SQL_QUANTITY = "SELECT * FROM " + QuantityDbDao.TABLENAME + " WHERE "
                + QuantityDbDao.Properties.Food_id.columnName + "=" + foodId + " AND "
                + QuantityDbDao.Properties.Quantity_id.columnName + "=" + quantity_id;
        Cursor cursor = App.getInstance().getmDaoSession().getAddOnDbDao().getDatabase().rawQuery(SQL_QUANTITY, null);

        return getAddedQuantityDetailsResult(cursor);
    }

    private List<QuantityDb> getAddedQuantityDetailsResult(Cursor c) {

        ArrayList<QuantityDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(QuantityDbDao.Properties.Food_id.columnName);
        final int quantity_id = c.getColumnIndex(QuantityDbDao.Properties.Quantity_id.columnName);
        final int price = c.getColumnIndex(QuantityDbDao.Properties.Price.columnName);
        final int name = c.getColumnIndex(QuantityDbDao.Properties.Name.columnName);
        final int id = c.getColumnIndex(QuantityDbDao.Properties.Id.columnName);
        final int rest_id = c.getColumnIndex(QuantityDbDao.Properties.Restaurant_id.columnName);
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String quantity_id_fin = c.getString(quantity_id);
                        final String price_fin = c.getString(price);
                        final String name_fin = c.getString(name);
                        final long id_fin = Long.parseLong(c.getString(id));

                        result.add(new QuantityDb(id_fin, food_id_fin, quantity_id_fin, rest_id_fin, name_fin, price_fin));
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        return result;
    }

//update foodTable ---------------------------------------------------------------------------------------------------------------------------------------

    private void intialiseRestIdDB() {
        cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();

        if (cartDetailsDbList.size() > 0) {
            restaurant_id = cartDetailsDbList.get(0).getRestaurant_id();
        } else {
            restaurant_id = "";
        }
    }


    private void AddFoodToDb() {
        if (restaurant_id.isEmpty() || restaurant_id.equals(CONST.restaren_id)) {
            getFoodDataQuery(finalFoodList);
            int count = foodItemDb.getFood_qty() + 1;
            Log.e(TAG, "AddFoodToDb:count "+count);
            foodItemDb.setFood_qty(count);
            setFoodDataQuery(foodItemDb);
        }
    }

    private void ReduceFoodInDb() {
        getFoodDataQuery(finalFoodList);
        if (foodItemDb.getFood_qty() > 1) {
            int count = foodItemDb.getFood_qty() - 1;
            foodItemDb.setFood_qty(count);
            setFoodDataQuery(foodItemDb);
        } else {
            foodItemDb.setFood_qty(0);
            setFoodDataQuery(foodItemDb);
        }
    }


    private void getFoodDataQuery(FinalFoodList pojo) {
        String SQL_SINGLE_DATA = "SELECT * FROM " + FoodItemDbDao.TABLENAME + " WHERE " + FoodItemDbDao.Properties.Food_id.columnName + "=";

        Cursor c = App.getInstance().getmDaoSession().getFoodItemDbDao().getDatabase().rawQuery(SQL_SINGLE_DATA + pojo.getFood_id(), null);

        //get Cart Count of preferred productId
        ArrayList<FoodItemDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(FoodItemDbDao.Properties.Food_id.columnName);
        final int food_cost = c.getColumnIndex(FoodItemDbDao.Properties.Food_cost.columnName);
        final int food_name = c.getColumnIndex(FoodItemDbDao.Properties.Food_name.columnName);
        final int food_count = c.getColumnIndex(FoodItemDbDao.Properties.Food_qty.columnName);
        final int food_type = c.getColumnIndex(FoodItemDbDao.Properties.Food_type.columnName);
        final int food_desc = c.getColumnIndex(FoodItemDbDao.Properties.Food_desc.columnName);
        final int food_tax = c.getColumnIndex(FoodItemDbDao.Properties.Food_tax.columnName);
        final int rest_id = c.getColumnIndex(FoodItemDbDao.Properties.Restaurant_id.columnName);
        final int target_amount = c.getColumnIndex(FoodItemDbDao.Properties.Target_amount.columnName);
        final int offer_amount = c.getColumnIndex(FoodItemDbDao.Properties.Offer_amount.columnName);
        final int discount_type = c.getColumnIndex(FoodItemDbDao.Properties.Discount_type.columnName);
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String food_name_fin = c.getString(food_name);
                        final String food_desc_fin = c.getString(food_desc);
                        final double food_cost_fin = Double.parseDouble(c.getString(food_cost));
                        final int food_count_fin = Integer.parseInt(c.getString(food_count));
                        final int food_type_fin = Integer.parseInt(c.getString(food_type));
                        final double food_tax_fin = Double.parseDouble(c.getString(food_tax));
                        final String target_amount_fin = c.getString(target_amount);
                        final String offer_amount_fin = c.getString(offer_amount);
                        final String discount_type_fin = c.getString(discount_type);
                        result.add(new FoodItemDb(rest_id_fin, food_id_fin, food_count_fin, food_name_fin, food_type_fin, food_cost_fin, food_desc_fin,food_tax_fin,discount_type_fin,target_amount_fin,offer_amount_fin));
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        if (result.size() > 0) {
            for (FoodItemDb foodItemDbsingle : result) {
                foodItemDb = foodItemDbsingle;
                Log.e("TAG", "onProductDetailsFragmentView:result foodItemDb.getFood_id " + foodItemDb.getFood_id());
                Log.e("TAG", "onProductDetailsFragmentView:result oodItemDb.getFood_qty" + foodItemDb.getFood_qty());
            }
        } else {
            foodItemDb = null;
        }

        if (foodItemDb == null) {
            foodItemDb = new FoodItemDb();
            foodItemDb.setFood_cost(Double.parseDouble(pojo.getPrice()));
            foodItemDb.setFood_id(pojo.getFood_id());
            foodItemDb.setFood_name(pojo.getName());
            foodItemDb.setFood_qty(pojo.getItem_count());
            foodItemDb.setFood_type(pojo.getIs_veg());
            foodItemDb.setFood_desc(pojo.getDescription());
            foodItemDb.setFood_tax(pojo.getItem_tax());
            foodItemDb.setTarget_amount(pojo.getTarget_amount());
            foodItemDb.setDiscount_type(pojo.getDiscount_type());
            foodItemDb.setOffer_amount(pojo.getOffer_amount());
            foodItemDb.setRestaurant_id(CONST.restaren_id);
        }

    }

    private void setFoodDataQuery(FoodItemDb foodItemDb) {
        CartDetailsDb cartDetailsDb = null;

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        if (foodItemDb.getFood_qty() == 0) {
            App.getInstance().getmDaoSession().getFoodItemDbDao().delete(foodItemDb);
            //added on May 2 2019
            /*if (App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll().size() <= 0) {
                App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
                App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
                App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
                App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
                Log.e(TAG, "intialiseRestIdDB:cartDetailsDbList size "+App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().size() );
                intialiseRestIdDB();
            }*/
            //////////////////

        } else {
            App.getInstance().getmDaoSession().getFoodItemDbDao().insertOrReplace(foodItemDb);
        }

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        double totalAmount = 0;
        int totalCount = 0;
        List<GroupDb> groupDbList = App.getInstance().getmDaoSession().getGroupDbDao().loadAll();
        Log.e(TAG, "setFoodDataQuery:groupDbList size "+groupDbList.size());
        for (GroupDb groupDb : groupDbList) {
            Log.e(TAG, "setFoodDataQuery:groupDb" + groupDb.getGroupamount());
            totalAmount = totalAmount + groupDb.getGroupamount();
            totalCount = totalCount + groupDb.getGroupCount();
        }

        //add data to local Db
        cartDetailsDb = new CartDetailsDb();
        cartDetailsDb.setRestaurant_id(CONST.restaren_id);
        cartDetailsDb.setTotalAmount("" + totalAmount);
        cartDetailsDb.setTotalCount("" + totalCount);
        cartDetailsDb.setRestaurant_name(restaurantData.getRestaurant_name());
        cartDetailsDb.setRestaurant_image(restaurantData.getImage());
        cartDetailsDb.setRestaurant_address(restaurantData.getAddress());
        cartDetailsDb.setDiscount_type(restaurantData.getDiscount_type());
        cartDetailsDb.setOffer_amount(restaurantData.getOffer_amount());
        cartDetailsDb.setTarget_amount(restaurantData.getTarget_amount());
        App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);

        CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
        Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
        Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());

        //added on May 2 2019
        if (App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll().size() <= 0) {
            App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
            App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
            App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
            App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
            App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
            App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
            Log.e(TAG, "intialiseRestIdDB:cartDetailsDbList size "+App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().size() );
            intialiseRestIdDB();
        }
        //////////////////


        /*List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            //add data to local Db
            cartDetailsDb = new CartDetailsDb();
            cartDetailsDb.setRestaurant_id(CONST.restaren_id);
            cartDetailsDb.setTotalAmount("" + totalAmount);
            cartDetailsDb.setTotalCount("" + totalCount);
            cartDetailsDb.setRestaurant_name(restaurantData.getRestaurant_name());
            cartDetailsDb.setRestaurant_image(restaurantData.getImage());
            cartDetailsDb.setRestaurant_address(restaurantData.getAddress());
            App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);

            CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
            Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
            Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());

        } else {
            if (foodItemDb.getFood_qty() != 0) {
                //add data to local Db
                cartDetailsDb = new CartDetailsDb();
                cartDetailsDb.setRestaurant_id(CONST.restaren_id);
                cartDetailsDb.setTotalAmount("" + foodItemDb.getFood_cost());
                cartDetailsDb.setTotalCount("" + foodItemDb.getFood_qty());
                cartDetailsDb.setRestaurant_name(restaurantData.getRestaurant_name());
                cartDetailsDb.setRestaurant_image(restaurantData.getImage());
                cartDetailsDb.setRestaurant_address(restaurantData.getAddress());
                App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);
                CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
                Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
                Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());
            }
        }*/

        if (adapterRefreshCallBack != null) {
            adapterRefreshCallBack.onDbUpdateAdapterRefresh();
        }

        if (oncartUpdate != null) {
            oncartUpdate.onUpdateCart();
        }

    }


    public RestaurantData getRestaurantDetails() {
        RestaurantData restaurantDataSingle = null;
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            restaurantDataSingle = new RestaurantData();
            restaurantDataSingle.setRestaurant_name(cartDetailsDbList.get(0).getRestaurant_name());
            restaurantDataSingle.setImage(cartDetailsDbList.get(0).getRestaurant_image());
            restaurantDataSingle.setAddress(cartDetailsDbList.get(0).getRestaurant_address());
            restaurantDataSingle.setOffer_amount(cartDetailsDbList.get(0).getOffer_amount());
            restaurantDataSingle.setTarget_amount(cartDetailsDbList.get(0).getTarget_amount());
            restaurantDataSingle.setDiscount_type(cartDetailsDbList.get(0).getDiscount_type());
        }
        return restaurantDataSingle;
    }


    //update Data In Cart


}
