import ExampleComponent from "../components/ExampleComponent";

export default function ExamplePage() {
	return (
		<div>
			<ExampleComponent num={1} />
			<ExampleComponent num={2} />
			<ExampleComponent num={3} />
		</div>
	);
}
