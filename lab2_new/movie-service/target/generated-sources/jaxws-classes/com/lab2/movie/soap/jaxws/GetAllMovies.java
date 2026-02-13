
package com.lab2.movie.soap.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getAllMovies", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllMovies", namespace = "http://soap.movie.lab2.com/", propOrder = {
    "page",
    "size",
    "sort",
    "id",
    "name",
    "oscarsCount",
    "goldenPalmCount",
    "length",
    "genre",
    "operatorName",
    "x",
    "y"
})
public class GetAllMovies {

    @XmlElement(name = "page", namespace = "")
    private int page;
    @XmlElement(name = "size", namespace = "")
    private int size;
    @XmlElement(name = "sort", namespace = "")
    private List<String> sort;
    @XmlElement(name = "id", namespace = "")
    private String id;
    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "oscarsCount", namespace = "")
    private String oscarsCount;
    @XmlElement(name = "goldenPalmCount", namespace = "")
    private String goldenPalmCount;
    @XmlElement(name = "length", namespace = "")
    private String length;
    @XmlElement(name = "genre", namespace = "")
    private String genre;
    @XmlElement(name = "operatorName", namespace = "")
    private String operatorName;
    @XmlElement(name = "x", namespace = "")
    private String x;
    @XmlElement(name = "y", namespace = "")
    private String y;

    /**
     * 
     * @return
     *     returns int
     */
    public int getPage() {
        return this.page;
    }

    /**
     * 
     * @param page
     *     the value for the page property
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getSize() {
        return this.size;
    }

    /**
     * 
     * @param size
     *     the value for the size property
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 
     * @return
     *     returns List&lt;String&gt;
     */
    public List<String> getSort() {
        return this.sort;
    }

    /**
     * 
     * @param sort
     *     the value for the sort property
     */
    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getId() {
        return this.id;
    }

    /**
     * 
     * @param id
     *     the value for the id property
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param name
     *     the value for the name property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOscarsCount() {
        return this.oscarsCount;
    }

    /**
     * 
     * @param oscarsCount
     *     the value for the oscarsCount property
     */
    public void setOscarsCount(String oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getGoldenPalmCount() {
        return this.goldenPalmCount;
    }

    /**
     * 
     * @param goldenPalmCount
     *     the value for the goldenPalmCount property
     */
    public void setGoldenPalmCount(String goldenPalmCount) {
        this.goldenPalmCount = goldenPalmCount;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getLength() {
        return this.length;
    }

    /**
     * 
     * @param length
     *     the value for the length property
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * 
     * @param genre
     *     the value for the genre property
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOperatorName() {
        return this.operatorName;
    }

    /**
     * 
     * @param operatorName
     *     the value for the operatorName property
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getX() {
        return this.x;
    }

    /**
     * 
     * @param x
     *     the value for the x property
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getY() {
        return this.y;
    }

    /**
     * 
     * @param y
     *     the value for the y property
     */
    public void setY(String y) {
        this.y = y;
    }

}
