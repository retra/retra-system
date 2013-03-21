<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<p>Pomocí tohoto formuláře se můžete zaregistrovat. Registrace
Vám umožní elektronické přihlašování na akce pořádané oblastními středisky:</p>

<ul>
	<li>ZDVPP a SSŠ Kladno</li>
	<li>ZDVPP a SSŠ Mladá Boleslav</li>
	<li>ZDVPP a SSŠ Příbram</li>
</ul>

<form action='PersonRegisrationStore.do?fkprm=true' method='post'>

	<table>
		<tr>
			<th>Jméno:</th>
			<td>
				<input type='text' name='firstName' />
			</td>
		</tr>
		<tr>
			<th>Příjmení:</th>
			<td>
				<input type='text' name='lastName' />
			</td>
		</tr>
		<tr>
			<th>Titul:</th>
			<td>
				<input type='text' name='degree' />
			</td>
		</tr>
		<tr>
			<th>Datum narození:</th>
			<td>
				<input type='text' name='birthDate' />
			</td>
		</tr>
		<tr>
			<th>Místo narození:</th>
			<td>
				<input type='text' name='birthPlace' />
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type='submit' name='buttonSubmit' value='Zaregistorvat' />
			</td>
		</tr>
	</table>

</form>

