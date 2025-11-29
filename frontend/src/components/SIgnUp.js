import React from "react";
import { SignUp } from "@clerk/clerk-react";

export default function SignUpPage() {
    return (
        <div style={{ display: "flex", justifyContent: "center", marginTop: "50px" }}>
            <SignUp path="/sign-up" routing="path" signInUrl="/sign-in" />
        </div>
    );
}
