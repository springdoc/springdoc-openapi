package org.springdoc.sample1;

import java.io.Serializable;

/**
 * @author 472957
 *
 */
public class ItemLightDTO implements Serializable {

    /**
     * serialVersionUID of type long
     */
    private static final long serialVersionUID = 1L;

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
    public ItemLightDTO() {
    }

    public ItemLightDTO(String description, int price) {
		super();
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
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
   * @param description description
   */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
   * @param price price
   */
    public void setPrice(final int price) {
        this.price = price;
    }

}
