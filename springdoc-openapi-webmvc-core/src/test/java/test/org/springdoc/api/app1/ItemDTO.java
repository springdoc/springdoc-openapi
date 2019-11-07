package test.org.springdoc.api.app1;

import java.io.Serializable;

/**
 * @author 472957
 */
public class ItemDTO implements Serializable {

    /**
     * serialVersionUID of type long
     */
    private static final long serialVersionUID = 1L;

    /**
     * itemID of type String
     */
    private String itemID;

    /**
     * description of type String
     */
    private String description;

    /**
     * price of type int
     */
    private int price;

    /**
     *
     */
    public ItemDTO() {
    }

    /**
     * @param description description
     * @param price       price
     */
    public ItemDTO(final String description, final int price) {
        this.description = description;
        this.price = price;
    }

    /**
     * @param itemID      itemID
     * @param description description
     * @param price       price
     */
    public ItemDTO(final String itemID, final String description, final int price) {
        this.itemID = itemID;
        this.description = description;
        this.price = price;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public String getItemID() {
        return itemID;
    }

    /**
     * @param itemID itemID
     */
    public void setItemID(final String itemID) {
        this.itemID = itemID;
    }

    /**
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price price
     */
    public void setPrice(final int price) {
        this.price = price;
    }

}
