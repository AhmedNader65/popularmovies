package mrerror.popularmovies.models;

import java.util.ArrayList;

/**
 * Created by ahmed on 19/07/16.
 */
public class Movies {
	private int movie_id;
	private String title ;
	private String thumbnail;
	private String synopsis;
	private String rating;
	private String date ;
	private String isFav ;
	private ArrayList<Trailer> trailers;
	private ArrayList<Review> reviews;

	public int getMovie_id() {
		return movie_id;
	}

	public String getIsFav() {
		return isFav;
	}

	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}

	public ArrayList<Trailer> getTrailers() {
		return trailers;
	}

	public void setTrailers(ArrayList<Trailer> trailers) {
		this.trailers = trailers;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
