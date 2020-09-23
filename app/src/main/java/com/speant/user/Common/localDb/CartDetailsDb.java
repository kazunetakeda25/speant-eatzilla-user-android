package com.speant.user.Common.localDb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "CartDetailsDb")
public class CartDetailsDb {

    @Id
    private String restaurant_id;

    private String restaurant_name;

    private String restaurant_image;

    private String restaurant_address;

    private String target_amount;

    private String offer_amount;

    private String discount_type;

    @ToMany(referencedJoinProperty = "restaurant_id")
    List<FoodItemDb> foodItemDbList;

    private String totalAmount;

    private String totalCount;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 457829327)
    private transient CartDetailsDbDao myDao;

    @Generated(hash = 573416495)
    public CartDetailsDb(String restaurant_id, String restaurant_name,
            String restaurant_image, String restaurant_address,
            String target_amount, String offer_amount, String discount_type,
            String totalAmount, String totalCount) {
        this.restaurant_id = restaurant_id;
        this.restaurant_name = restaurant_name;
        this.restaurant_image = restaurant_image;
        this.restaurant_address = restaurant_address;
        this.target_amount = target_amount;
        this.offer_amount = offer_amount;
        this.discount_type = discount_type;
        this.totalAmount = totalAmount;
        this.totalCount = totalCount;
    }

    @Generated(hash = 1601207286)
    public CartDetailsDb() {
    }

    public String getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getRestaurant_name() {
        return this.restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_image() {
        return this.restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }

    public String getRestaurant_address() {
        return this.restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getTarget_amount() {
        return this.target_amount;
    }

    public void setTarget_amount(String target_amount) {
        this.target_amount = target_amount;
    }

    public String getOffer_amount() {
        return this.offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    public String getDiscount_type() {
        return this.discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 635093455)
    public List<FoodItemDb> getFoodItemDbList() {
        if (foodItemDbList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FoodItemDbDao targetDao = daoSession.getFoodItemDbDao();
            List<FoodItemDb> foodItemDbListNew = targetDao
                    ._queryCartDetailsDb_FoodItemDbList(restaurant_id);
            synchronized (this) {
                if (foodItemDbList == null) {
                    foodItemDbList = foodItemDbListNew;
                }
            }
        }
        return foodItemDbList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2054506501)
    public synchronized void resetFoodItemDbList() {
        foodItemDbList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 558343137)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCartDetailsDbDao() : null;
    }

   





}
