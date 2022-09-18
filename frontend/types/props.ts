/* 추상화된 컴포넌트들의 props 타입 */

import { ElementType } from "react";
import { OverridableProps } from "@/types/utils";

export type TextBaseProps = {
  typography?: string;
};
export type TextProps<T extends ElementType> = OverridableProps<
  T,
  TextBaseProps
>;

export type ButtonBaseProps = {
  size?: "fit" | "full";
  type?: "purple" | "blue";
};
export type ButtonProps<T extends ElementType> = OverridableProps<
  T,
  ButtonBaseProps
>;