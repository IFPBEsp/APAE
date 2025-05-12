import Image from "next/image";

import Styles from "./Dot.module.css";

interface IDotProps {
    title: string,
    image: string,
    imageDescription: string
}

export default function Dot ({ title, image, imageDescription }: IDotProps) {
    return (
        <div className={Styles.container}>
            <p>{title}</p>
            <Image src={image} alt={imageDescription} width={100} height={70} className={Styles.image} />
        </div>
    );
}