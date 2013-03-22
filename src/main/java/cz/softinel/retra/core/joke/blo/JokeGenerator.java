package cz.softinel.retra.core.joke.blo;

import java.util.Date;
import java.util.List;

import cz.softinel.retra.core.joke.Joke;

public interface JokeGenerator {

	public List<Joke> getJokesForDate(Date date);
	
}
