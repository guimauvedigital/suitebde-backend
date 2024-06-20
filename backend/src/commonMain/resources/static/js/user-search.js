const dropdownMenu = document.getElementById('user-dropdown-menu');
const searchInput = document.getElementById('user-dropdown-input');
const targetInput = document.getElementById(searchInput.getAttribute('targetId'));

const associationId = searchInput.getAttribute('associationId');
const classes = "block px-4 py-2 text-gray-700 hover:bg-gray-100 active:bg-blue-100 cursor-pointer rounded-md".split(" ")

// Fetch users with the given search term
const fetchUsers = async (searchTerm) => {
    const response = await fetch(`/api/v1/associations/${associationId}/users?search=${searchTerm}`);
    return response.json();
}

// Add event listener to filter items based on input
searchInput.addEventListener('input', () => {
    dropdownMenu.querySelectorAll('a').forEach((item) => item.remove());
    fetchUsers(searchInput.value).then((users) => {
        users.forEach((user) => {
            const item = document.createElement('a');
            item.classList.add(...classes);
            item.textContent = `${user.firstName} ${user.lastName}`;
            item.onclick = () => {
                searchInput.value = `${user.firstName} ${user.lastName}`;
                targetInput.value = user.id;
                dropdownMenu.querySelectorAll('a').forEach((item) => item.remove());
            };
            dropdownMenu.appendChild(item);
        });
    });
});
