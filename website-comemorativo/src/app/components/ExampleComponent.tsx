interface IExampleComponent {
	num: number;
}

export default function ExampleComponent({ num }: IExampleComponent) {
	return (
		<div>
			<p>Exemplo {num}</p>
		</div>
	);
}
