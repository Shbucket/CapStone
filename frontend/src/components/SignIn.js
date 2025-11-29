import React from "react";
import { SignIn } from "@clerk/clerk-react";

export default function SignInPage() {
    return (
        <div style={{ display: "flex", justifyContent: "center", marginTop: "50px" }}>
            <SignIn path="/sign-in" routing="path" signUpUrl="/sign-up" />
        </div>
    );
}