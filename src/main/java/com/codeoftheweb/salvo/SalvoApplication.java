package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			playerRepository.save(player1);
				Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			playerRepository.save(player2);
				Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			playerRepository.save(player3);
			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));
			playerRepository.save(player4);


			Game game1 = new Game(LocalDateTime.now());
			gameRepository.save(game1);
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			gameRepository.save(game2);
			Game game3 = new Game(LocalDateTime.now().plusHours(2));
			gameRepository.save(game3);
            Game game4 = new Game(LocalDateTime.now().plusHours(3));
            gameRepository.save(game4);
            Game game5 = new Game(LocalDateTime.now().plusHours(4));
            gameRepository.save(game5);
            Game game6 = new Game(LocalDateTime.now().plusHours(5));
            gameRepository.save(game6);
            Game game7 = new Game(LocalDateTime.now().plusHours(6));
            gameRepository.save(game7);
            Game game8 = new Game(LocalDateTime.now().plusHours(7));
            gameRepository.save(game8);

            Ship ship1 = new Ship("Baquetas", Arrays.asList("H2", "H3", "H4"));
			Ship ship2 = new Ship("Microfono", Arrays.asList("E1", "F1", "G1"));
			Ship ship3 = new Ship("Trompeta", Arrays.asList("B4", "B5"));
			Ship ship4 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship5 = new Ship("Trompeta", Arrays.asList("F1", "F2"));
			Ship ship6 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship7 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship8 = new Ship("Microfono", Arrays.asList("A2", "A3", "A4"));
			Ship ship9 = new Ship("Trompeta", Arrays.asList("G6", "H6"));
			Ship ship10 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship11 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship12 = new Ship("Microfono", Arrays.asList("A2", "A3", "A4"));
			Ship ship13 = new Ship("Trompeta", Arrays.asList("G6", "H6"));
			Ship ship14 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship15 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship16 = new Ship("Microfono", Arrays.asList("A2", "A3", "A4"));
			Ship ship17 = new Ship("Trompeta", Arrays.asList("G6", "H6"));
			Ship ship18 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship19 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship20 = new Ship("Microfono", Arrays.asList("A2", "A3", "A4"));
			Ship ship21 = new Ship("Trompeta", Arrays.asList("G6", "H6"));
			Ship ship22 = new Ship("Destoyer", Arrays.asList("B5", "C5", "D5"));
			Ship ship23 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship24 = new Ship("Baquetas", Arrays.asList("B5", "C5", "D5"));
			Ship ship25 = new Ship("Trompeta", Arrays.asList("C6", "C7"));
			Ship ship26 = new Ship("Microfono", Arrays.asList("A2", "A3", "A4"));
			Ship ship27 = new Ship("Trompeta", Arrays.asList("G6 , H6"));
			Ship ship28 = new Ship("Piano", Arrays.asList("A1", "A2", "A3", "A4", "A5"));
			Ship ship29 = new Ship("Guitarra", Arrays.asList("C1", "C2", "C3", "C4"));

			Salvo salvo1 = new Salvo(1, Arrays.asList("B5", "C5", "F1"));
			Salvo salvo2 = new Salvo(1, Arrays.asList("B4", "B5", "B6"));
			Salvo salvo3 = new Salvo(2, Arrays.asList("F2", "D5"));
			Salvo salvo4 = new Salvo(2, Arrays.asList("E1", "H3", "A2")); //game 1
			Salvo salvo5 = new Salvo(1, Arrays.asList("A2", "A4", "G6"));
			Salvo salvo6 = new Salvo(1, Arrays.asList("B5", "D5", "C7"));
			Salvo salvo7 = new Salvo(2, Arrays.asList("A3", "H6"));
			Salvo salvo8 = new Salvo(2, Arrays.asList("C5", "C6")); // game 2
			Salvo salvo9 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
			Salvo salvo10 = new Salvo(1, Arrays.asList("H1", "H2", "H3"));
			Salvo salvo11 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
			Salvo salvo12 = new Salvo(2, Arrays.asList("E1", "F2", "G3")); // game 3
			Salvo salvo13 = new Salvo(1, Arrays.asList("A3", "A4", "F7"));
			Salvo salvo14 = new Salvo(1, Arrays.asList("B5", "C6", "H1"));
			Salvo salvo15 = new Salvo(2, Arrays.asList("A2", "G6", "H6"));
			Salvo salvo16 = new Salvo(2, Arrays.asList("C5", "C7", "D5")); // game 4
			Salvo salvo17 = new Salvo(1, Arrays.asList("A1", "A2", "A3"));
			Salvo salvo18 = new Salvo(1, Arrays.asList("B5", "B6", "C7"));
			Salvo salvo19 = new Salvo(2, Arrays.asList("G5", "G7", "G8"));
			Salvo salvo20 = new Salvo(2, Arrays.asList("C6", "D6", "E6"));
			Salvo salvo21 = new Salvo(3, Arrays.asList("H1", "H8")); // game 5

			Score score1 = new Score(LocalDateTime.now(), game1, player1, 1.0);
			scoreRepository.save(score1);
			Score score2 = new Score(LocalDateTime.now(), game1, player2, 0.0);
			scoreRepository.save(score2);
			Score score3= new Score(LocalDateTime.now().plusHours(1), game2, player1, 0.5);
			scoreRepository.save(score3);
			Score score4= new Score(LocalDateTime.now().plusHours(1), game2, player2, 0.5);
			scoreRepository.save(score4);
			Score score5= new Score(LocalDateTime.now().plusHours(2), game3, player2, 1.0);
			scoreRepository.save(score5);
			Score score6= new Score(LocalDateTime.now().plusHours(2), game3, player4, 0.0);
			scoreRepository.save(score6);
			Score score7= new Score(LocalDateTime.now().plusHours(3), game4, player2, 0.5);
			scoreRepository.save(score7);
			Score score8= new Score(LocalDateTime.now().plusHours(3), game4, player1, 0.5);
			scoreRepository.save(score8);


            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);
            gamePlayer1.addShip(ship28);
            gamePlayer1.addShip(ship29);
            gamePlayer1.addSalvo(salvo1);
            gamePlayer1.addSalvo(salvo3);

            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
			gamePlayer2.addShip(ship4);
			gamePlayer2.addShip(ship5);
			gamePlayer2.addSalvo(salvo2);
			gamePlayer2.addSalvo(salvo4);

            GamePlayer gamePlayer3 = new GamePlayer(game2, player1);
            gamePlayer3.addShip(ship6);
            gamePlayer3.addShip(ship7);
			gamePlayer3.addSalvo(salvo5);
			gamePlayer3.addSalvo(salvo7);

            GamePlayer gamePlayer4 = new GamePlayer(game2, player2);
			gamePlayer4.addShip(ship8);
			gamePlayer4.addShip(ship9);
			gamePlayer4.addSalvo(salvo6);
			gamePlayer4.addSalvo(salvo8);

            GamePlayer gamePlayer5 = new GamePlayer(game3, player2);
            gamePlayer5.addShip(ship10);
            gamePlayer5.addShip(ship11);
            gamePlayer5.addShip(ship12);
            gamePlayer5.addShip(ship13);
			gamePlayer5.addSalvo(salvo9);
			gamePlayer5.addSalvo(salvo11);

            GamePlayer gamePlayer6 = new GamePlayer(game3, player4);
            gamePlayer6.addShip(ship14);
            gamePlayer6.addShip(ship15);
			gamePlayer6.addSalvo(salvo10);
			gamePlayer6.addSalvo(salvo12);

            GamePlayer gamePlayer7 = new GamePlayer(game4, player2);
            gamePlayer7.addShip(ship16);
            gamePlayer7.addShip(ship17);
			gamePlayer7.addSalvo(salvo13);
			gamePlayer7.addSalvo(salvo15);

            GamePlayer gamePlayer8 = new GamePlayer(game4, player1);
            gamePlayer8.addShip(ship18);
            gamePlayer8.addShip(ship19);
			gamePlayer8.addSalvo(salvo14);
			gamePlayer8.addSalvo(salvo16);

            GamePlayer gamePlayer9 = new GamePlayer(game5, player4);
			gamePlayer9.addShip(ship20);
			gamePlayer9.addShip(ship21);
			gamePlayer9.addSalvo(salvo17);
			gamePlayer9.addSalvo(salvo19);

            GamePlayer gamePlayer10 = new GamePlayer(game5, player1);
			gamePlayer10.addShip(ship22);
			gamePlayer10.addShip(ship23);
			gamePlayer10.addSalvo(salvo18);
			gamePlayer10.addSalvo(salvo20);
			gamePlayer10.addSalvo(salvo21);

            GamePlayer gamePlayer11 = new GamePlayer(game6, player3);
            gamePlayer11.addShip(ship24);
            gamePlayer11.addShip(ship25);

            GamePlayer gamePlayer12 = new GamePlayer(game7, player4);

            GamePlayer gamePlayer13 = new GamePlayer(game8, player3);
            gamePlayer13.addShip(ship26);
            gamePlayer13.addShip(ship27);

            GamePlayer gamePlayer14 = new GamePlayer(game8, player4);



			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer13);
			gamePlayerRepository.save(gamePlayer14);

		};
	}
}

//autenticación
@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputUserName-> {
			Player player = playerRepository.findByUserName(inputUserName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputUserName);
			}
		});
	}
}

//autorizacion
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/game_view/**", "/web/game.html").hasAuthority("USER")
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/**").permitAll();

		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

	}








